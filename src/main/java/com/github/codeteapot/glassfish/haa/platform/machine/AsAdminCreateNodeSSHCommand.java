package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;

class AsAdminCreateNodeSSHCommand implements MachineCommand<Void> {

  private final String asAdminPath;
  private final AsAdminTarget target;
  private final String name;
  private final String nodeHost;
  private final String installDir;
  private final String nodeDir;
  private final Integer sshPort;
  private final String sshKeyFile;
  private final Boolean force;
  private final Boolean install;

  AsAdminCreateNodeSSHCommand(
      String asAdminPath,
      AsAdminTarget target,
      String name,
      String nodeHost,
      String installDir,
      String nodeDir,
      Integer sshPort,
      String sshKeyFile,
      Boolean force,
      Boolean install) {
    this.asAdminPath = requireNonNull(asAdminPath);
    this.target = requireNonNull(target);
    this.name = requireNonNull(name);
    this.nodeHost = requireNonNull(nodeHost);
    this.installDir = installDir;
    this.nodeDir = nodeDir;
    this.sshPort = sshPort;
    this.sshKeyFile = sshKeyFile;
    this.force = force;
    this.install = install;
  }

  @Override
  public String getSentence() {
    StringBuilder sentence = new StringBuilder();
    sentence.append(asAdminPath);
    target.sentenceAppend(sentence);
    sentence.append(" create-node-ssh");
    sentence.append(" --nodehost=").append(nodeHost);
    ofNullable(installDir).ifPresent(value -> sentence.append(" --installDir=").append(value));
    ofNullable(nodeDir).ifPresent(value -> sentence.append(" --nodeDir=").append(value));
    ofNullable(sshPort).ifPresent(value -> sentence.append(" --sshport=").append(value));
    ofNullable(sshKeyFile).ifPresent(value -> sentence.append(" --sshkeyfile=").append(value));
    ofNullable(force).ifPresent(value -> sentence.append(" --force=").append(value));
    ofNullable(install).ifPresent(value -> sentence.append(" --install=").append(value));
    sentence.append(" ").append(name);
    return sentence.toString();
  }

  @Override
  public MachineCommandExecution<Void> getExecution(MachineCommandExecutionContext context) {
    return new AsAdminCreateNodeSSHCommandExecution(context);
  }
}
