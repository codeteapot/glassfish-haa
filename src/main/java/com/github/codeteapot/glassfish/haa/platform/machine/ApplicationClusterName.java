package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class ApplicationClusterName implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String value;

  @ConstructorProperties({
      "value"
  })
  public ApplicationClusterName(String value) {
    this.value = requireNonNull(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof ApplicationClusterName) {
      ApplicationClusterName clusterName = (ApplicationClusterName) obj;
      return value.equals(clusterName.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return value;
  }

  boolean hasValue(String value) {
    return value.equals(this.value);
  }
}
