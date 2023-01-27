package com.github.codeteapot.ironhoist.port;

public interface PlatformPort {

  void listen(MachineManager manager) throws InterruptedException;
}
