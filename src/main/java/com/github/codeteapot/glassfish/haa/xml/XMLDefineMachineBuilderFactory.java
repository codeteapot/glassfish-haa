package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.MachineBuilderFactory;
import com.github.codeteapot.ironhoist.MachineBuilder;

class XMLDefineMachineBuilderFactory extends XMLMachineBuilder
    implements MachineBuilderFactory {

  @Override
  public MachineBuilder getBuilder(MachineBuilderDeserializer deserializer) {
    return this;
  }
}
