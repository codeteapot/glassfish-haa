package com.github.codeteapot.glassfish.haa.platform;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.MachineBuilder;
import com.github.codeteapot.ironhoist.MachineBuilderContext;
import com.github.codeteapot.ironhoist.MachineBuildingException;
import java.io.IOException;
import java.io.InputStream;

public abstract class InputMachineBuilder implements MachineBuilder {

  private final MachineBuilderDeserializer deserializer;

  protected InputMachineBuilder(MachineBuilderDeserializer deserializer) {
    this.deserializer = requireNonNull(deserializer);
  }

  protected void build(MachineBuilderContext context, InputStream input)
      throws MachineBuildingException {
    try {
      deserializer.deserialize(input).build(context);
    } catch (IOException e) {
      throw new MachineBuildingException(e);
    }
  }
}
