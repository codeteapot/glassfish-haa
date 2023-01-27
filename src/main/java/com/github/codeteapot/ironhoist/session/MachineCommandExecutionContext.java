package com.github.codeteapot.ironhoist.session;

import java.io.IOException;
import java.nio.charset.Charset;

public interface MachineCommandExecutionContext {

  Charset charset();

  void put(byte[] buffer, int len) throws IOException;
}
