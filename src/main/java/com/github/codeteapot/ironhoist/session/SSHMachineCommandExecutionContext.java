package com.github.codeteapot.ironhoist.session;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

class SSHMachineCommandExecutionContext implements MachineCommandExecutionContext {

  private final OutputStream input;

  SSHMachineCommandExecutionContext(OutputStream input) {
    this.input = requireNonNull(input);
  }

  @Override
  public Charset charset() {
    return defaultCharset();
  }

  @Override
  public void put(byte[] buffer, int len) throws IOException {
    input.write(buffer, 0, len);
    input.flush();
  }
}
