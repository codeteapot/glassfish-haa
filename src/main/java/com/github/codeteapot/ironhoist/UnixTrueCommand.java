package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class UnixTrueCommand implements MachineCommand<Boolean> {

  UnixTrueCommand() {}

  @Override
  public String getSentence() {
    return "true";
  }

  @Override
  public MachineCommandExecution<Boolean> getExecution(MachineCommandExecutionContext context) {
    return new UnixTrueCommandExecution(context);
  }
}
