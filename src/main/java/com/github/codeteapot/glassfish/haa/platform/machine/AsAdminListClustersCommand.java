package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import java.util.Set;

class AsAdminListClustersCommand implements MachineCommand<Set<AsAdminClusterEntry>> {

  private final String asAdminPath;
  private final AsAdminTarget target;

  AsAdminListClustersCommand(String asAdminPath, AsAdminTarget target) {
    this.asAdminPath = requireNonNull(asAdminPath);
    this.target = requireNonNull(target);
  }

  @Override
  public String getSentence() {
    StringBuilder sentence = new StringBuilder();
    sentence.append(asAdminPath);
    target.sentenceAppend(sentence);
    sentence.append(" list-clusters");
    return sentence.toString();
  }

  @Override
  public MachineCommandExecution<Set<AsAdminClusterEntry>> getExecution(
      MachineCommandExecutionContext context) {
    return new AsAdminListClustersCommandExecution(context);
  }
}
