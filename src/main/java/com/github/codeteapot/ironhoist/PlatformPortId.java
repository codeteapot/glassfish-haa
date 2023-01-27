package com.github.codeteapot.ironhoist;

import static java.math.BigInteger.valueOf;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.port.MachineId;
import java.util.Random;

class PlatformPortId {

  private static final int VALUE_BOUND = 1000;

  private final int value;

  PlatformPortId(Random random) {
    value = random.nextInt(VALUE_BOUND);
  }

  MachineRef machineRef(MachineId id) {
    return id.machineRef(valueOf(value).toByteArray());
  }
  
  @Override
  public int hashCode() {
    return value % 20;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof PlatformPortId) {
      PlatformPortId id = (PlatformPortId) obj;
      return value == id.value;
    }
    return false;
  }
}
