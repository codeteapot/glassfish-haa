package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

class ApplicationClusterBean implements ApplicationCluster {

  private final ApplicationClusterName name;

  ApplicationClusterBean(ApplicationClusterName name) {
    this.name = requireNonNull(name);
  }

  @Override
  public ApplicationClusterName getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof ApplicationClusterBean) {
      ApplicationClusterBean cluster = (ApplicationClusterBean) obj;
      return name.equals(cluster.name);
    }
    return false;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("Cluster ").append(name)
        .toString();
  }

  boolean hasNameValue(String nameValue) {
    return name.hasValue(nameValue);
  }
}
