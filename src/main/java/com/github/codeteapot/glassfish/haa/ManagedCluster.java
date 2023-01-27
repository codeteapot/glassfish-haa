package com.github.codeteapot.glassfish.haa;

import static com.github.codeteapot.ironhoist.Machine.facetFilter;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationCluster;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomain;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationNodeName;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministration;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.beans.PropertyChangeListenerProxy;
import java.util.Set;
import java.util.logging.Logger;

class ManagedCluster implements ManagedClusterMBean {

  private static final Logger logger = getLogger(ManagedCluster.class.getName());

  private final PlatformContext context;
  private final SSHAuthorizationRegistry authorizationRegistry;
  private final ExpectationSatisfier expectationSatisfier;
  private final Cluster managed;

  ManagedCluster(
      PlatformContext context,
      SSHAuthorizationRegistry authorizationRegistry,
      ExpectationSatisfier expectationSatisfier,
      Cluster managed) {
    this.context = requireNonNull(context);
    this.authorizationRegistry = requireNonNull(authorizationRegistry);
    this.expectationSatisfier = requireNonNull(expectationSatisfier);
    this.managed = requireNonNull(managed);
    authorizationRegistry = new SSHAuthorizationRegistry(context);
  }

  void machineAvailable(ApplicationServerAdministration administration) {
    if (managed.managedBy(administration)) {
      administration.getDomains()
          .stream()
          .filter(managed::managedBy)
          .forEach(domain -> domainAvailable(administration, domain));
      administration.addPropertyChangeListener(
          new PropertyChangeListenerProxy("domains", event -> {
            Set<?> oldValue = (Set<?>) event.getOldValue();
            Set<?> newValue = (Set<?>) event.getNewValue();
            newValue.stream()
                .map(ApplicationDomain.class::cast)
                .filter(managed::managedBy)
                .filter(domain -> !oldValue.contains(domain))
                .findAny()
                .ifPresent(domain -> domainAvailable(administration, domain));
          }));
    }
  }

  void machineAvailable(ApplicationServerExecution execution) {
    if (managed.managedBy(execution)) {
      context.available()
          .flatMap(facetFilter(ApplicationServerFacet.class))
          .flatMap(ApplicationServerFacet::filterAdministration)
          .filter(managed::managedBy)
          .forEach(administration -> administration.getDomains()
              .stream()
              .filter(managed::managedBy)
              .findAny()
              .ifPresent(domain -> expectationSatisfier.add(new NodeCreatedExpectation(
                  authorizationRegistry,
                  new SSHPublicKeyProxy(context, administration.getMachineRef()),
                  administration.getMachineRef(),
                  execution.getMachineRef(),
                  managed::managedBy,
                  this::nodeNameMapper))));
    }
  }

  void machineLost(MachineRef machineRef) {
    authorizationRegistry.unauthorize(machineRef);
    context.available()
        .flatMap(facetFilter(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::filterAdministration)
        .filter(managed::managedBy)
        .forEach(administration -> expectationSatisfier.add(new NodeDeletedExpectation(
            administration.getMachineRef(),
            machineRef,
            managed::managedBy,
            this::nodeNameMapper)));
  }

  private void domainAvailable(
      ApplicationServerAdministration administration,
      ApplicationDomain domain) {
    logger.fine(new StringBuilder()
        .append("Domain ").append(domain.getName()).append(" available")
        .toString());
    if (domain.isRunning()) {
      domainReady(administration, domain);
    }
    domain.addPropertyChangeListener(new PropertyChangeListenerProxy("running", event -> {
      if ((boolean) event.getNewValue()) {
        domainReady(administration, domain);
      }
    }));
    domain.addPropertyChangeListener(new PropertyChangeListenerProxy("clusters", event -> {
      Set<?> oldValue = (Set<?>) event.getOldValue();
      Set<?> newValue = (Set<?>) event.getNewValue();
      newValue.stream()
          .map(ApplicationCluster.class::cast)
          .filter(managed::managedBy)
          .filter(cluster -> !oldValue.contains(cluster))
          .forEach(cluster -> clusterAvailable(administration, domain, cluster));
    }));
  }

  private void domainReady(
      ApplicationServerAdministration administration,
      ApplicationDomain domain) {
    logger.fine(new StringBuilder()
        .append("Domain ").append(domain.getName()).append(" is ready")
        .toString());
    SSHPublicKeyProxy publicKey = new SSHPublicKeyProxy(context, administration.getMachineRef());
    context.available()
        .flatMap(facetFilter(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::filterExecution)
        .filter(managed::managedBy)
        .forEach(execution -> expectationSatisfier.add(new NodeCreatedExpectation(
            authorizationRegistry,
            publicKey,
            administration.getMachineRef(),
            execution.getMachineRef(),
            managed::managedBy,
            this::nodeNameMapper)));
    domain.getClusters().forEach(cluster -> clusterAvailable(administration, domain, cluster));
  }

  private void clusterAvailable(
      ApplicationServerAdministration administration,
      ApplicationDomain domain,
      ApplicationCluster cluster) {
    logger.fine(new StringBuilder()
        .append("Cluster ").append(cluster.getName()).append(" is available")
        .toString());
    domain.getNodes().forEach(node -> logger.fine(new StringBuilder()
        .append("Node ").append(node.getName())
        .append(" available for cluster ").append(cluster.getName())
        .toString()));
    // TODO Continue feature development: Create instances
  }

  private ApplicationNodeName nodeNameMapper(MachineRef machineRef) {
    return new ApplicationNodeName(new StringBuilder()
        .append("haa-")
        .append(machineRef.toString().replace(':', '-'))
        .toString());
  }
}
