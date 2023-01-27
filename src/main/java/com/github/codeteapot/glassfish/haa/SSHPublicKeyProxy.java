package com.github.codeteapot.glassfish.haa;

import static com.github.codeteapot.ironhoist.Machine.facetGet;
import static java.util.Objects.requireNonNull;

import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerException;
import com.github.codeteapot.glassfish.haa.platform.machine.ApplicationServerFacet;
import com.github.codeteapot.glassfish.haa.platform.machine.SSHPublicKey;
import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;

class SSHPublicKeyProxy {

  private final PlatformContext context;
  private final MachineRef administrationRef;
  private SSHPublicKey value;

  SSHPublicKeyProxy(PlatformContext context, MachineRef administrationRef) {
    this.context = requireNonNull(context);
    this.administrationRef = requireNonNull(administrationRef);
    value = null;
  }

  MachineRef getAdministrationRef() {
    return administrationRef;
  }

  SSHPublicKey getValue() throws ApplicationServerException {
    if (value == null) {
      value = context.lookup(administrationRef)
          .flatMap(facetGet(ApplicationServerFacet.class))
          .flatMap(ApplicationServerFacet::getAdministration)
          .orElseThrow(() -> new IllegalStateException("Not an administration server machine"))
          .getPublicKey();
    }
    return value;
  }
}
