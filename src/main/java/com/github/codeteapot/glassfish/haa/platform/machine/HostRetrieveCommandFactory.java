package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;

class HostRetrieveCommandFactory {

  private final String path;

  HostRetrieveCommandFactory(String path) {
    this.path = requireNonNull(path);
  }

  MachineCommand<String> retrieveHost() {
    return new RetrieveHostCommand(path);
  }
}
