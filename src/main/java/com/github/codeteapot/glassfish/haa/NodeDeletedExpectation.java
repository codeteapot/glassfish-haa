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
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

class NodeDeletedExpectation implements Expectation {

  private static final Logger logger = getLogger(NodeDeletedExpectation.class.getName());

  private final MachineRef administrationRef;
  private final MachineRef executionRef;
  private final Predicate<ApplicationDomain> domainFilter;
  private final Function<MachineRef, ApplicationNodeName> nodeNameMapper;

  NodeDeletedExpectation(
      MachineRef administrationRef,
      MachineRef executionRef,
      Predicate<ApplicationDomain> domainFilter,
      Function<MachineRef, ApplicationNodeName> nodeNameMapper) {
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
        .map(this::satisfy)
        .orElse(true);
  }

  private boolean satisfy(ApplicationServerAdministration administration) {
    return administration.getDomains()
        .stream()
        .filter(domainFilter)
        .findAny()
        .map(this::satisfy)
        .orElse(true);
  }

  private boolean satisfy(ApplicationDomain domain) {
    if (domain.isRunning()) {
      return false;
    }
    if (domain.getNodes()
        .stream()
        .map(ApplicationNode::getName)
        .noneMatch(nodeNameMapper.apply(executionRef)::equals)) {
      return true;
    }
    try {
      domain.deleteNode(nodeNameMapper.apply(executionRef));
      logger.fine(new StringBuilder()
          .append("Node ").append(nodeNameMapper.apply(executionRef)).append(" deleted")
          .toString());
      return true;
    } catch (ApplicationDomainException e) {
      logger.info("Unable to delete node");
      logger.log(FINER, "Unable to delete node", e);
      return false;
    }
  }
}
