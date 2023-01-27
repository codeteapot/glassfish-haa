package com.github.codeteapot.glassfish.haa.platform;

import com.github.codeteapot.ironhoist.MachineBuilder;

public interface MachineBuilderFactory {

  MachineBuilder getBuilder(MachineBuilderDeserializer deserializer);
}
