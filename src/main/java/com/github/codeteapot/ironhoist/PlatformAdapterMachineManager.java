package com.github.codeteapot.ironhoist;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.port.MachineId;
import com.github.codeteapot.ironhoist.port.MachineLink;
import com.github.codeteapot.ironhoist.port.MachineManager;

class PlatformAdapterMachineManager implements MachineManager {

  private final PlatformPortId portId;
  private final MachineContainer container;

  public PlatformAdapterMachineManager(
      PlatformPortIdRegistry portIdRegistry,
      MachineContainer container) {
    portId = portIdRegistry.newPortId();
    this.container = requireNonNull(container);
  }

  @Override
  public void take(MachineId id, MachineLink link) {
    container.take(portId.machineRef(id), link);
  }

  @Override
  public void forget(MachineId id) {
    container.forget(portId.machineRef(id));
  }
}
