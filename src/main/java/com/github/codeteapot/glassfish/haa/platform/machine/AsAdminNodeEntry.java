package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Optional;

class AsAdminNodeEntry {
  
  private final String name;
  private final String type;
  private final String host;
  private final String installDir;
  private final String referencedBy;
  
  AsAdminNodeEntry(String name, String type, String host, String installDir, String referencedBy) {
    this.name = requireNonNull(name);
    this.type = requireNonNull(type);
    this.host = requireNonNull(host);
    this.installDir = requireNonNull(installDir);
    this.referencedBy = referencedBy;
  }
  
  String getName() {
    return name;
  }
  
  String getType() {
    return type;
  }
  
  String getHost() {
    return host;
  }
  
  String getInstallDir() {
    return installDir;
  }
  
  Optional<String> getReferencedBy() {
    return ofNullable(referencedBy);
  }
}
