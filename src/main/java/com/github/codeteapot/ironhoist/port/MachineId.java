package com.github.codeteapot.ironhoist.port;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineRef;

public class MachineId {

  private final byte[] value;

  public MachineId(byte[] value) {
    this.value = requireNonNull(value);
  }

  public MachineRef machineRef(byte[] portId) {
    return new MachineRef(portId, value);
  }
}
