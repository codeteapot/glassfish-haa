package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Optional;

public class ApplicationNodeRequirements {

  private final String nodeHost;
  private final String installDir;
  private final String nodeDir;

  ApplicationNodeRequirements(String nodeHost, String installDir, String nodeDir) {
    this.nodeHost = requireNonNull(nodeHost);
    this.installDir = installDir;
    this.nodeDir = nodeDir;
  }

  public String getNodeHost() {
    return nodeHost;
  }

  public Optional<String> getInstallDir() {
    return ofNullable(installDir);
  }

  public Optional<String> getNodeDir() {
    return ofNullable(nodeDir);
  }
}
