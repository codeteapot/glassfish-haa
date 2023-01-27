package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

class AsAdminClusterEntry {

  private final String name;

  AsAdminClusterEntry(String name) {
    this.name = requireNonNull(name);
  }

  String getName() {
    return name;
  }
}
