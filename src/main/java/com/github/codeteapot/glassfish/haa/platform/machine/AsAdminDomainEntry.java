package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

class AsAdminDomainEntry {

  private final String name;
  private final String adminHost;
  private final int adminPort;
  private final boolean running;
  private final boolean restartRequired;

  AsAdminDomainEntry(
      String name,
      String adminHost,
      int adminPort,
      boolean running,
      boolean restartRequired) {
    this.name = requireNonNull(name);
    this.adminHost = requireNonNull(adminHost);
    this.adminPort = adminPort;
    this.running = running;
    this.restartRequired = restartRequired;
  }

  String getName() {
    return name;
  }
  
  String getAdminHost() {
    return adminHost;
  }
  
  int getAdminPort() {
    return adminPort;
  }

  boolean isRunning() {
    return running;
  }

  boolean isRestartRequired() {
    return restartRequired;
  }
}
