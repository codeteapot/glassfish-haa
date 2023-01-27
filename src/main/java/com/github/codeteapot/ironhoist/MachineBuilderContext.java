package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;

public interface MachineBuilderContext {

  MachineSession getSession(String username)
      throws UnknownUserException, MachineSessionHostResolutionException;

  public void register(MachineFacetFactory factory) throws MachineFacetInstantiationException;
}
