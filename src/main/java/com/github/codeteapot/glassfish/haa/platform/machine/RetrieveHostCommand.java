package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class RetrieveHostCommand implements MachineCommand<String> {

  private final String path;

  RetrieveHostCommand(String path) {
    this.path = requireNonNull(path);
  }

  @Override
  public String getSentence() {
    return path;
  }

  @Override
  public MachineCommandExecution<String> getExecution(MachineCommandExecutionContext context) {
    return new RetrieveHostCommandExecution(context);
  }
}
