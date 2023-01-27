package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class ApplicationDomainName implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String value;

  @ConstructorProperties({
      "value"
  })
  public ApplicationDomainName(String value) {
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
    if (obj instanceof ApplicationDomainName) {
      ApplicationDomainName domainName = (ApplicationDomainName) obj;
      return value.equals(domainName.value);
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
