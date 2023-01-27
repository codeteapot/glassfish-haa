package com.github.codeteapot.glassfish.haa.platform;

import com.github.codeteapot.ironhoist.session.AbstractMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;

class SimpleMachineReadyCheckCommandExecution extends AbstractMachineCommandExecution<Boolean> {

  // TODO Configurable success exit codes
  private static final int DEFAULT_SUCCESS_EXIT_CODE = 0;

  protected SimpleMachineReadyCheckCommandExecution(MachineCommandExecutionContext context) {
    super(context);
  }

  @Override
  public Boolean mapResult(int exitCode) throws MachineSessionException {
    return exitCode == DEFAULT_SUCCESS_EXIT_CODE;
  }
}
