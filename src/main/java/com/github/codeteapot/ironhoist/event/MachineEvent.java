package com.github.codeteapot.ironhoist.event;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.EventObject;

public abstract class MachineEvent extends EventObject {

  private static final long serialVersionUID = 1L;

  private final MachineRef machineRef;

  protected MachineEvent(Object source, MachineRef machineRef) {
    super(source);
    this.machineRef = requireNonNull(machineRef);
  }

  public MachineRef getMachineRef() {
    return machineRef;
  }
}
