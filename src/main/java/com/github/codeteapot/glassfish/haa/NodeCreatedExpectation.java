package com.github.codeteapot.glassfish.haa;

import static com.github.codeteapot.ironhoist.Machine.facetGet;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.FINER;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomain;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationDomainException;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationNode;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationNodeName;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerAdministration;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerException;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

class NodeCreatedExpectation implements Expectation {

  private static final Logger logger = getLogger(NodeCreatedExpectation.class.getName());

  private final SSHAuthorizationRegistry authorizationRegistry;
  private final SSHPublicKeyProxy publicKey;
  private final MachineRef administrationRef;
  private final MachineRef executionRef;
  private final Predicate<ApplicationDomain> domainFilter;
  private final Function<MachineRef, ApplicationNodeName> nodeNameMapper;

  NodeCreatedExpectation(
      SSHAuthorizationRegistry authorizationRegistry,
      SSHPublicKeyProxy publicKey,
      MachineRef administrationRef,
      MachineRef executionRef,
      Predicate<ApplicationDomain> domainFilter,
      Function<MachineRef, ApplicationNodeName> nodeNameMapper) {
    this.authorizationRegistry = requireNonNull(authorizationRegistry);
    this.publicKey = requireNonNull(publicKey);
    this.administrationRef = requireNonNull(administrationRef);
    this.executionRef = requireNonNull(executionRef);
    this.domainFilter = requireNonNull(domainFilter);
    this.nodeNameMapper = requireNonNull(nodeNameMapper);
  }

  @Override
  public boolean satisfy(PlatformContext context) {
    return context.lookup(administrationRef)
        .flatMap(facetGet(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::getAdministration)
        .map(administration -> satisfy(context, administration))
        .orElse(false);
  }

  @Override
  public int hashCode() {
    return administrationRef.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof NodeCreatedExpectation) {
      NodeCreatedExpectation expectation = (NodeCreatedExpectation) obj;
      return administrationRef.equals(expectation.administrationRef) &&
          executionRef.equals(expectation.executionRef);
    }
    return false;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Node created for domain administered by ").append(administrationRef)
        .append(" for execution ").append(executionRef)
        .toString();
  }

  private boolean satisfy(PlatformContext context, ApplicationServerAdministration administration) {
    return context.lookup(executionRef)
        .flatMap(facetGet(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::getExecution)
        .map(execution -> satisfy(administration, execution))
        .orElse(false); // It can't be never satisfied
  }

  private boolean satisfy(
      ApplicationServerAdministration administration,
      ApplicationServerExecution execution) {
    return administration.getDomains()
        .stream()
        .filter(domainFilter)
        .filter(ApplicationDomain::isRunning)
        .findAny()
        .map(domain -> satisfy(domain, execution))
        .orElse(false);
  }

  private boolean satisfy(
      ApplicationDomain domain,
      ApplicationServerExecution execution) {
    if (!domain.isRunning()) {
      return false;
    }
    if (domain.getNodes()
        .stream()
        .map(ApplicationNode::getName)
        .anyMatch(nodeNameMapper.apply(executionRef)::equals)) {
      logger.fine(new StringBuilder()
          .append("Node ").append(nodeNameMapper.apply(executionRef))
          .append(" already created")
          .toString());
      return true;
    }
    try {
      authorizationRegistry.authorize(publicKey, execution);
      domain.createNode(
          nodeNameMapper.apply(executionRef),
          execution.getNodeRequirements());
      logger.fine(new StringBuilder()
          .append("Node ").append(nodeNameMapper.apply(executionRef)).append(" created")
          .toString());
      return true;
    } catch (ApplicationDomainException | ApplicationServerException e) {
      logger.info("Unable to create node");
      logger.log(FINER, "Unable to create node", e);
      return false;
    }
  }
}
