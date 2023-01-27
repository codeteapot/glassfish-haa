package com.github.codeteapot.glassfish.haa.platform;

import com.github.codeteapot.ironhoist.MachineBuilder;
import java.io.IOException;
import java.io.InputStream;

public interface MachineBuilderDeserializer {

  MachineBuilder deserialize(InputStream input) throws IOException;
}
