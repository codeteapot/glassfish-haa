package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.ironhoist.session.BufferedMachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

class AsAdminListClustersCommandExecution
    extends BufferedMachineCommandExecution<Set<AsAdminClusterEntry>> {

  private static final Set<String> EXCLUDED_LINES = Stream.of(
      "Nothing to list",
      "Command list-clusters executed successfully.").collect(toSet());

  private final Set<AsAdminClusterEntry> result;

  AsAdminListClustersCommandExecution(MachineCommandExecutionContext context) {
    super(context);
    result = new HashSet<>();
  }

  @Override
  protected void acceptOutput(String line) throws IOException {
    String cleanLine = line.trim();
    if (!cleanLine.isEmpty() && !EXCLUDED_LINES.contains(cleanLine)) {
      String[] parts = cleanLine.split("\\s+");
      result.add(new AsAdminClusterEntry(parts[0]));
    }
  }

  @Override
  protected Set<AsAdminClusterEntry> mapCompleteResult(int exitCode)
      throws MachineSessionException {
    if (exitCode == 0) {
      return result;
    }
    throw new MachineSessionException("Unable to list clusters");
  }
}
