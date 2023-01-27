package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class GetPasswordsCommand implements MachineCommand<String> {

  private final String path;
  private final String code;

  GetPasswordsCommand(String path, String code) {
    this.path = requireNonNull(path);
    this.code = requireNonNull(code);
  }

  @Override
  public String getSentence() {
    return new StringBuilder().append(path).append(" ").append(code).append(" get").toString();
  }

  @Override
  public MachineCommandExecution<String> getExecution(MachineCommandExecutionContext context) {
    return new GetPasswordsCommandExecution(context);
  }
}
