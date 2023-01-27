package com.github.codeteapot.glassfish.haa.platform.machine;

import com.github.codeteapot.ironhoist.session.BufferedMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;

class GetPasswordsCommandExecution extends BufferedMachineCommandExecution<String> {

  private String result;

  GetPasswordsCommandExecution(MachineCommandExecutionContext context) {
    super(context);
    result = null;
  }

  @Override
  protected void acceptOutput(String line) throws IOException {
    if (result == null && !line.trim().isEmpty()) {
      result = line;
    }
  }

  @Override
  protected String mapCompleteResult(int exitCode) throws MachineSessionException {
    if (exitCode == 0) {
      if (result == null) {
        throw new MachineSessionException("Passwords are not available");
      }
      return result;
    }
    throw new MachineSessionException("Unable to get passwords");
  }
}
