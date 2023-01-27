package com.github.codeteapot.ironhoist;

import java.time.Duration;
import java.util.Optional;

public interface MachineSessionPool {

  Optional<Duration> getIdleTimeout();
}
