package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.session.AbstractMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;

class UnixTrueCommandExecution extends AbstractMachineCommandExecution<Boolean> {

  private static final int SUCCESS_EXIT_CODE = 0;

  protected UnixTrueCommandExecution(MachineCommandExecutionContext context) {
    super(context);
  }

  @Override
  public Boolean mapResult(int exitCode) throws MachineSessionException {
    return exitCode == SUCCESS_EXIT_CODE;
  }
}
