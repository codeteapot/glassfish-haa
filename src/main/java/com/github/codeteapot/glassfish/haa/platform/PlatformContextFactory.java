package com.github.codeteapot.glassfish.haa.platform;

import com.github.codeteapot.ironhoist.PlatformContext;
import com.github.codeteapot.ironhoist.PlatformEventSource;
import java.util.concurrent.ExecutorService;

public interface PlatformContextFactory {

  PlatformContext getContext(
      MachineBuilderDeserializer machineBuilderDeserializer,
      PlatformEventSource eventSource,
      ExecutorService listenerExecutor);
}
