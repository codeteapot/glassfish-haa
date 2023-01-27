package com.github.codeteapot.glassfish.haa;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerException;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerExecution;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.HashMap;
import java.util.Map;

class SSHAuthorizationRegistry {

  private final PlatformContext context;
  private final Map<MachineRef, SSHAuthorizationRegistryRecord> recordMap;

  SSHAuthorizationRegistry(PlatformContext context) {
    this.context = requireNonNull(context);
    recordMap = new HashMap<>();
  }

  boolean authorize(SSHPublicKeyProxy publicKey, ApplicationServerExecution execution)
      throws ApplicationServerException {
    recordMap.merge(
        publicKey.getAdministrationRef(),
        new SSHAuthorizationRegistryRecord(context, publicKey.getValue()),
        (machineRef, record) -> record.authorize(execution));
    return false;
  }

  void unauthorize(MachineRef administrationRef) {
    ofNullable(recordMap.remove(administrationRef))
        .ifPresent(SSHAuthorizationRegistryRecord::unauthorize);
  }
}
