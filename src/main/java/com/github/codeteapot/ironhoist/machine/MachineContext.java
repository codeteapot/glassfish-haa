package com.github.codeteapot.ironhoist.machine;

import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;

public interface MachineContext {

  MachineRef getRef();

  MachineSession getSession(String username)
      throws UnknownUserException, MachineSessionHostResolutionException;
}
