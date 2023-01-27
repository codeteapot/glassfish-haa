package com.github.codeteapot.glassfish.haa.xml;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.MachineBuilderFactory;
import com.github.codeteapot.ironhoist.MachineBuilder;

class XMLIncludeMachineBuilderFactory extends XMLMachineBuilder
    implements MachineBuilderFactory {

  @Override
  public MachineBuilder getBuilder(MachineBuilderDeserializer deserializer) {
    throw new UnsupportedOperationException();
  }
}
