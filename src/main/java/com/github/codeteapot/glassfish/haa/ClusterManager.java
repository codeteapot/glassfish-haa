package com.github.codeteapot.glassfish.haa;

import static com.github.codeteapot.ironhoist.Machine.facetGet;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.event.MachineAvailableEvent;
import com.github.codeteapot.ironhoist.event.MachineLostEvent;
import com.github.codeteapot.ironhoist.event.PlatformListener;
import java.util.Set;
import java.util.logging.Logger;

public class ClusterManager implements ClusterManagerMBean, PlatformListener {

  private static final Logger logger = getLogger(ClusterManager.class.getName());

  private final PlatformContext context;
  private final Set<ManagedCluster> clusters;
  private final ExpectationSatisfier expectationSatisfier;

  public ClusterManager(PlatformContext context, Set<Cluster> clusters) {
    this.context = requireNonNull(context);
    SSHAuthorizationRegistry authorizationRegistry = new SSHAuthorizationRegistry(context);
    expectationSatisfier = new ExpectationSatisfier(context);
    this.clusters = clusters.stream()
        .map(cluster -> new ManagedCluster(
            context,
            authorizationRegistry,
            expectationSatisfier,
            cluster))
        .collect(toSet());
  }

  @Override
  public void machineAvailable(MachineAvailableEvent event) {
    logger.fine(format("Machine %s is available", event.getMachineRef()));
    context.lookup(event.getMachineRef())
        .flatMap(facetGet(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::getAdministration)
        .ifPresent(administration -> clusters.forEach(
            cluster -> cluster.machineAvailable(administration)));
    context.lookup(event.getMachineRef())
        .flatMap(facetGet(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::getExecution)
        .ifPresent(execution -> clusters.forEach(
            cluster -> cluster.machineAvailable(execution)));
  }

  @Override
  public void machineLost(MachineLostEvent event) {
    logger.fine(format("Machine %s was lost", event.getMachineRef()));
    clusters.forEach(cluster -> cluster.machineLost(event.getMachineRef()));
  }

  void shutdown() {
    expectationSatisfier.shutdown();
  }
}
