package com.github.codeteapot.ironhoist.session;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

public abstract class AbstractMachineCommandExecution<R> implements MachineCommandExecution<R> {

  protected final MachineCommandExecutionContext context;

  protected AbstractMachineCommandExecution(MachineCommandExecutionContext context) {
    this.context = requireNonNull(context);
  }

  @Override
  public void acceptOutput(byte[] bytes, int len) throws IOException {}

  @Override
  public void acceptError(byte[] bytes, int len) throws IOException {}
}
