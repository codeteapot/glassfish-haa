package com.github.codeteapot.glassfish.haa;

import com.github.codeteapot.ironhoist.PlatformContext;

public interface ClusterManagerFactory {

  ClusterManager getClusterManager(PlatformContext context);
}
