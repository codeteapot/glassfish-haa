package com.github.codeteapot.ironhoist.machine;

import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Arrays;

public class MachineRef implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

  private final byte[] portId;
  private final byte[] machineId;

  @ConstructorProperties({
      "portId",
      "machineId"
  })
  public MachineRef(byte[] portId, byte[] machineId) {
    this.portId = requireNonNull(portId);
    this.machineId = requireNonNull(machineId);
  }

  public byte[] getPortId() {
    return portId;
  }

  public byte[] getMachineId() {
    return machineId;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(machineId);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MachineRef) {
      MachineRef ref = (MachineRef) obj;
      return Arrays.equals(portId, ref.portId) && Arrays.equals(machineId, ref.machineId);
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    appendHex(str, portId)
        .append(':');
    appendHex(str, machineId);
    return str.toString();
  }

  private StringBuilder appendHex(StringBuilder str, byte[] bytes) {
    for (int i = 0; i < bytes.length; ++i) {
      int b = bytes[i] & 0xff;
      str.append(HEX_ARRAY[b >>> 4]).append(HEX_ARRAY[b & 0x0f]);
    }
    return str;
  }
}
