package com.github.codeteapot.ironhoist;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineFacet;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import java.util.HashSet;
import java.util.Set;

class ManagedMachineFactoryImpl implements ManagedMachineFactory, MachineBuilderContext {

  private final MachineContext context;
  private final Set<MachineFacet> facets;

  ManagedMachineFactoryImpl(MachineContext context) {
    this.context = requireNonNull(context);
    facets = new HashSet<>();
  }

  @Override
  public void build(MachineBuilder builder) throws MachineBuildingException {
    facets.clear();
    builder.build(this);
  }

  @Override
  public ManagedMachine getMachine(MachineSessionPoolReleaser sessionPoolReleaser) {
    return new ManagedMachineImpl(sessionPoolReleaser, facets);
  }

  @Override
  public MachineSession getSession(String username)
      throws UnknownUserException, MachineSessionHostResolutionException {
    return context.getSession(username);
  }

  @Override
  public void register(MachineFacetFactory factory) throws MachineFacetInstantiationException {
    facets.add(factory.getFacet(context));
  }
}
