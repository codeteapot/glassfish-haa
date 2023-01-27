package com.github.codeteapot.ironhoist.event;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.beans.ConstructorProperties;

public class MachineLostEvent extends MachineEvent {

  private static final long serialVersionUID = 1L;

  @ConstructorProperties({
      "machineRef"
  })
  public MachineLostEvent(Object source, MachineRef machineRef) {
    super(source, machineRef);
  }
}
