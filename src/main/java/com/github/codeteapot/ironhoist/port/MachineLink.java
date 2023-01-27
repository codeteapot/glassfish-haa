package com.github.codeteapot.ironhoist.port;

import java.net.InetAddress;
import java.util.Optional;

public interface MachineLink {

  Optional<MachineProfileName> getProfileName();

  InetAddress getSessionHost(MachineNetworkName networkName)
      throws MachineSessionHostResolutionException;
}
