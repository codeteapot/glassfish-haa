package com.github.codeteapot.ironhoist.event;

import java.util.EventListener;

public interface PlatformListener extends EventListener {
  
  void machineAvailable(MachineAvailableEvent event);
  
  void machineLost(MachineLostEvent event);
}
