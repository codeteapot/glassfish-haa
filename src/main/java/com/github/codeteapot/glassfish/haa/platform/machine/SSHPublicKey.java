package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

public class SSHPublicKey {

  private final String value;

  SSHPublicKey(String value) {
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
    if (obj instanceof SSHPublicKey) {
      SSHPublicKey publicKey = (SSHPublicKey) obj;
      return value.equals(publicKey.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return value;
  }
}
