package com.github.codeteapot.ironhoist;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class PlatformPortIdRegistry {

  private final Random random;
  private final Set<PlatformPortId> alreadyUsed;

  PlatformPortIdRegistry() {
    random = new Random();
    alreadyUsed = new HashSet<>();
  }

  PlatformPortId newPortId() {
    PlatformPortId portId = new PlatformPortId(random);
    while (alreadyUsed.contains(portId)) {
      portId = new PlatformPortId(random);
    }
    alreadyUsed.add(portId);
    return portId;
  }
}
