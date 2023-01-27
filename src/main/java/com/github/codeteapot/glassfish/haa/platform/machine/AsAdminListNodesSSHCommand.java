package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import java.util.Set;

class AsAdminListNodesSSHCommand implements MachineCommand<Set<AsAdminNodeEntry>> {

  private final String asAdminPath;
  private final AsAdminTarget target;

  AsAdminListNodesSSHCommand(String asAdminPath, AsAdminTarget target) {
    this.asAdminPath = requireNonNull(asAdminPath);
    this.target = requireNonNull(target);
  }

  @Override
  public String getSentence() {
    StringBuilder sentence = new StringBuilder();
    sentence.append(asAdminPath);
    target.sentenceAppend(sentence);
    sentence.append(" list-nodes-ssh");
    sentence.append(" --terse=true");
    sentence.append(" --long=true");
    return sentence.toString();
  }

  @Override
  public MachineCommandExecution<Set<AsAdminNodeEntry>> getExecution(
      MachineCommandExecutionContext context) {
    return new AsAdminListNodesSSHCommandExecution(context);
  }
}
