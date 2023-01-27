package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import com.github.codeteapot.ironhoist.session.BufferedMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

class AsAdminListDomainsCommandExecution
    extends BufferedMachineCommandExecution<Set<AsAdminDomainEntry>> {

  private final Set<AsAdminDomainEntry> result;

  AsAdminListDomainsCommandExecution(MachineCommandExecutionContext context) {
    super(context);
    result = new HashSet<>();
  }

  @Override
  protected void acceptOutput(String line) throws IOException {
    String cleanLine = line.trim();
    if (!cleanLine.isEmpty()) {
      String[] parts = cleanLine.split("\\s+");
      result.add(new AsAdminDomainEntry(
          parts[0],
          parts[1],
          parseInt(parts[2]),
          parseBoolean(parts[3]),
          parseBoolean(parts[4])));
    }
  }

  @Override
  protected Set<AsAdminDomainEntry> mapCompleteResult(int exitCode) throws MachineSessionException {
    if (exitCode == 0) {
      return result;
    }
    throw new MachineSessionException("Unable to list domains");
  }
}
