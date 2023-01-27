package com.github.codeteapot.ironhoist;

interface ManagedMachineFactory {

  void build(MachineBuilder builder) throws MachineBuildingException;

  ManagedMachine getMachine(MachineSessionPoolReleaser sessionPoolReleaser)
      throws MachineBuildingException;
}
