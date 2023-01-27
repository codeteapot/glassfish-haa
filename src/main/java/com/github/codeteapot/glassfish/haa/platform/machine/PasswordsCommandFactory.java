package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;

class PasswordsCommandFactory {

  private final String path;

  PasswordsCommandFactory(String path) {
    this.path = requireNonNull(path);
  }

  MachineCommand<String> getPasswords(String code) {
    return new GetPasswordsCommand(path, code);
  }

  MachineCommand<Void> removePasswords(String code) {
    return new RemovePasswordsCommand(path, code);
  }
}
