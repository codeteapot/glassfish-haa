package com.github.codeteapot.ironhoist.port;

public interface MachineManager {

  void take(MachineId id, MachineLink link);
  
  void forget(MachineId id);
}
