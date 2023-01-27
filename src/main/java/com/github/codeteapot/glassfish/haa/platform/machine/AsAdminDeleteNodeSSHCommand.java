package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class AsAdminDeleteNodeSSHCommand implements MachineCommand<Void> {

  private final String asAdminPath;
  private final AsAdminTarget target;
  private final String name;

  AsAdminDeleteNodeSSHCommand(String asAdminPath, AsAdminTarget target, String name) {
    this.asAdminPath = requireNonNull(asAdminPath);
    this.target = requireNonNull(target);
    this.name = requireNonNull(name);
  }

  @Override
  public String getSentence() {
    StringBuilder sentence = new StringBuilder();
    sentence.append(asAdminPath);
    target.sentenceAppend(sentence);
    sentence.append(" delete-node-ssh");
    sentence.append(" ").append(name);
    return sentence.toString();
  }

  @Override
  public MachineCommandExecution<Void> getExecution(MachineCommandExecutionContext context) {
    return new AsAdminDeleteNodeSSHCommandExecution(context);
  }
}
