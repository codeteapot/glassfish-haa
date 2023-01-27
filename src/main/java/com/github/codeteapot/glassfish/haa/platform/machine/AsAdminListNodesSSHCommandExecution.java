package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.ironhoist.session.BufferedMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

class AsAdminListNodesSSHCommandExecution
    extends BufferedMachineCommandExecution<Set<AsAdminNodeEntry>> {

  private static final Set<String> EXCLUDED_LINES = Stream.of(
      "Nothing to list",
      "Command list-nodes-ssh executed successfully.").collect(toSet());
  
  private final Set<AsAdminNodeEntry> result;
  private boolean headerLine;

  AsAdminListNodesSSHCommandExecution(MachineCommandExecutionContext context) {
    super(context);
    result = new HashSet<>();
    headerLine = true;
  }

  @Override
  protected void acceptOutput(String line) throws IOException {
    String cleanLine = line.trim();
    if (!cleanLine.isEmpty()) {
      if (headerLine) {
        headerLine = false;
      } else if (!EXCLUDED_LINES.contains(cleanLine)) {
        String[] parts = cleanLine.split("\\s+");
        result.add(new AsAdminNodeEntry(
            parts[0],
            parts[1],
            parts[2],
            parts[3],
            parts.length > 4 ? parts[4] : null));
      }
    }
  }

  @Override
  protected Set<AsAdminNodeEntry> mapCompleteResult(int exitCode) throws MachineSessionException {
    if (exitCode == 0) {
      return result;
    }
    throw new MachineSessionException("Unable to list nodes");
  }
}
