package com.github.codeteapot.glassfish.haa.platform.machine;

public interface ApplicationInstance {

  ApplicationInstanceName getName();
  
  ApplicationClusterName getClusterName();
}
