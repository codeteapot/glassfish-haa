package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.port.MachineNetworkName;
import java.util.Optional;

public interface MachineProfile {

  MachineNetworkName getNetworkName();

  Optional<Integer> getSessionPort();

  MachineRealm getRealm();
  
  MachineReadyCheck getReadyCheck();
  
  MachineSessionPool getSessionPool();
  
  MachineBuilder getBuilder();
}
