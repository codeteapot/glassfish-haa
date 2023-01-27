package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import java.util.Set;

class AsAdminCommandFactory {

  private final String asAdminPath;

  AsAdminCommandFactory(String asAdminPath) {
    this.asAdminPath = requireNonNull(asAdminPath);
  }

  MachineCommand<Set<AsAdminDomainEntry>> listDomains() {
    return new AsAdminListDomainsCommand(asAdminPath);
  }

  MachineCommand<Set<AsAdminClusterEntry>> listClusters(AsAdminTarget target) {
    return new AsAdminListClustersCommand(asAdminPath, target);
  }

  MachineCommand<Set<AsAdminNodeEntry>> listNodesSSH(AsAdminTarget target) {
    return new AsAdminListNodesSSHCommand(asAdminPath, target);
  }

  MachineCommand<Void> createNodeSSH(
      AsAdminTarget target,
      String name,
      String nodeHost,
      String installDir,
      String nodeDir,
      Integer sshPort,
      String sshKeyFile,
      Boolean force,
      Boolean install) {
    return new AsAdminCreateNodeSSHCommand(
        asAdminPath,
        target,
        name,
        nodeHost,
        installDir,
        nodeDir,
        sshPort,
        sshKeyFile,
        force,
        install);
  }

  MachineCommand<Void> deleteNodeSSH(AsAdminTarget target, String name) {
    return new AsAdminDeleteNodeSSHCommand(asAdminPath, target, name);
  }
}
