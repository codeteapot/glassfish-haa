package com.github.codeteapot.glassfish.haa.platform.machine;

@FunctionalInterface
public interface MachineMonitorRefresher {

  void refresh() throws Exception;
}
