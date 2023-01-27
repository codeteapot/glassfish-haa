package com.github.codeteapot.glassfish.haa;

import static com.github.codeteapot.ironhoist.Machine.facetGet;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerException;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.glassfish.haa.platform.machine.SSHPublicKey;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

class SSHAuthorizationRegistryRecord {

  private static final Logger logger = getLogger(SSHAuthorizationRegistryRecord.class.getName());

  private final PlatformContext context;
  private final SSHPublicKey publicKey;
  private final Set<MachineRef> executionRefSet;

  SSHAuthorizationRegistryRecord(PlatformContext context, SSHPublicKey publicKey) {
    this.context = requireNonNull(context);
    this.publicKey = requireNonNull(publicKey);
    executionRefSet = new HashSet<>();
  }

  SSHAuthorizationRegistryRecord authorize(ApplicationServerExecution execution) {
    try {
      if (execution.addAuthorizedKey(publicKey)) {
        logger.fine(new StringBuilder()
            .append("Public key ").append(publicKey)
            .append(" added to ").append(execution.getMachineRef())
            .toString());
        executionRefSet.add(execution.getMachineRef());
      }
      return this;
    } catch (ApplicationServerException e) {
      logger.log(SEVERE, "Could not add public key to execution", e);
      return null;
    }
  }

  void unauthorize() {
    executionRefSet.removeIf(executionRef -> context.lookup(executionRef)
        .flatMap(facetGet(ApplicationServerFacet.class))
        .flatMap(ApplicationServerFacet::getExecution)
        .map(this::unauthorize)
        .orElse(false));
  }

  private boolean unauthorize(ApplicationServerExecution execution) {
    try {
      if (execution.removeAuthorizedKey(publicKey)) {
        logger.fine(new StringBuilder()
            .append("Public key ").append(publicKey)
            .append(" removed from ").append(execution.getMachineRef())
            .toString());
      }
      return true;
    } catch (ApplicationServerException e) {
      logger.log(SEVERE, "Could not remove public key from execution", e);
      return false;
    }
  }
}
