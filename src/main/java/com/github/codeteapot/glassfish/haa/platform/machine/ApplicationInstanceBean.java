package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

class ApplicationInstanceBean implements ApplicationInstance {

  private final ApplicationInstanceName name;
  private final ApplicationClusterName clusterName;

  ApplicationInstanceBean(ApplicationInstanceName name, ApplicationClusterName clusterName) {
    this.name = requireNonNull(name);
    this.clusterName = requireNonNull(clusterName);
  }

  @Override
  public ApplicationInstanceName getName() {
    return name;
  }

  @Override
  public ApplicationClusterName getClusterName() {
    return clusterName;
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
    if (obj instanceof ApplicationInstanceBean) {
      ApplicationInstanceBean cluster = (ApplicationInstanceBean) obj;
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
}
