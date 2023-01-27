package com.github.codeteapot.ironhoist.session;

import java.net.InetAddress;

public interface MachineSessionFactory {

  MachineSession getSession(
      InetAddress host,
      Integer port,
      String username,
      MachineSessionAuthentication authentication);
}
