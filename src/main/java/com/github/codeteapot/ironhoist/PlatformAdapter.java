package com.github.codeteapot.ironhoist;

import static java.util.concurrent.Executors.newCachedThreadPool;

import com.github.codeteapot.ironhoist.port.PlatformPort;
import com.github.codeteapot.ironhoist.session.MachineSessionFactory;

public class PlatformAdapter {

  private final PlatformPortIdRegistry portIdRegistry;
  private final MachineContainer container;

  public PlatformAdapter(
      PlatformEventSource eventSource,
      MachineCatalog catalog,
      MachineSessionFactory sessionFactory) {
    container = new MachineContainer(
        eventSource,
        catalog,
        sessionFactory,
        newCachedThreadPool());
    portIdRegistry = new PlatformPortIdRegistry();
  }

  public PlatformContext getContext() {
    return container.getContext();
  }

  public void listen(PlatformPort port) throws InterruptedException {
    port.listen(new PlatformAdapterMachineManager(portIdRegistry, container));
  }
}
