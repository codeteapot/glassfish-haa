package com.github.codeteapot.ironhoist.session;

import java.io.IOException;

public interface MachineCommandExecution<R> {

  void acceptOutput(byte[] bytes, int len) throws IOException;

  void acceptError(byte[] bytes, int len) throws IOException;

  R mapResult(int exitCode) throws MachineSessionException;
}
