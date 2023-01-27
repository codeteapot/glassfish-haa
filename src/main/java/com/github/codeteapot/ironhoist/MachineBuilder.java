package com.github.codeteapot.ironhoist;

public interface MachineBuilder {

  void build(MachineBuilderContext context) throws MachineBuildingException;
}
