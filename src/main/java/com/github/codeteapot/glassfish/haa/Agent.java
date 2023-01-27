package com.github.codeteapot.glassfish.haa;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newCachedThreadPool;

import com.github.codeteapot.glassfish.haa.platform.MachineBuilderDeserializer;
import com.github.codeteapot.glassfish.haa.platform.PlatformContextFactory;
import com.github.codeteapot.ironhoist.PlatformEventQueue;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.OperationsException;

public class Agent {

  private static final String CLUSTER_MANAGER_MBEAN_NAME_VALUE =
      "com.github.codeteapot.glassfish.haa:type=basic,name=ClusterManager";

  private final PlatformContextFactory platformContextFactory;
  private final ClusterManagerFactory clusterManagerFactory;

  public Agent(
      PlatformContextFactory platformContextFactory,
      ClusterManagerFactory clusterManagerFactory) {
    this.platformContextFactory = requireNonNull(platformContextFactory);
    this.clusterManagerFactory = requireNonNull(clusterManagerFactory);
  }

  void accept(MachineBuilderDeserializer machineBuilderDeserializer, MBeanServer beanServer)
      throws InterruptedException {
    PlatformEventQueue eventQueue = new PlatformEventQueue();
    accept(beanServer, eventQueue, clusterManagerFactory.getClusterManager(
        platformContextFactory.getContext(
            machineBuilderDeserializer,
            eventQueue,
            newCachedThreadPool())));
  }

  private void accept(
      MBeanServer beanServer,
      PlatformEventQueue eventQueue,
      ClusterManager clusterManager) throws InterruptedException {
    try {
      beanServer.registerMBean(clusterManager, new ObjectName(CLUSTER_MANAGER_MBEAN_NAME_VALUE));
      eventQueue.addListener(clusterManager);
      eventQueue.dispatchEvents();
    } catch (MBeanRegistrationException | OperationsException e) {
      throw new IllegalStateException(e);
    } finally {
      clusterManager.shutdown();
    }
  }
}
