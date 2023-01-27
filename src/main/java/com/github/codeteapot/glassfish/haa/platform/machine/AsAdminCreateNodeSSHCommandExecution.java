package com.github.codeteapot.glassfish.haa.platform.machine;

import com.github.codeteapot.ironhoist.session.AbstractMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;

class AsAdminCreateNodeSSHCommandExecution extends AbstractMachineCommandExecution<Void> {

  protected AsAdminCreateNodeSSHCommandExecution(MachineCommandExecutionContext context) {
    super(context);
  }

  @Override
  public Void mapResult(int exitCode) throws MachineSessionException {
    if (exitCode == 0) {
      return null;
    }
    throw new MachineSessionException("Unable to create node");
  }
}
