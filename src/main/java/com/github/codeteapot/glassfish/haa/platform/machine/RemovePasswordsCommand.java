package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class RemovePasswordsCommand implements MachineCommand<Void> {

  private final String path;
  private final String code;

  RemovePasswordsCommand(String path, String code) {
    this.path = requireNonNull(path);
    this.code = requireNonNull(code);
  }

  @Override
  public String getSentence() {
    return new StringBuilder().append(path).append(" ").append(code).append(" remove").toString();
  }

  @Override
  public MachineCommandExecution<Void> getExecution(MachineCommandExecutionContext context) {
    return new RemovePasswordsCommandExecution(context);
  }
}
