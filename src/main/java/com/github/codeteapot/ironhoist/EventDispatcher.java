package com.github.codeteapot.ironhoist;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

class EventDispatcher<E extends EventObject, L extends EventListener> {

  private static final Logger logger = getLogger(EventDispatcher.class.getName());

  private final E event;
  private final Collection<L> listeners;
  private final BiConsumer<L, E> action;

  EventDispatcher(E event, Collection<L> listeners, BiConsumer<L, E> action) {
    this.event = requireNonNull(event);
    this.listeners = unmodifiableCollection(listeners);
    this.action = requireNonNull(action);
  }

  void dispatch() {
    listeners.forEach(this::dispatch);
    // TODO Reproduce and fix
    // SEVERE: Error occurred while dispatching event
    // java.util.ConcurrentModificationException
    //     at java.util.HashMap$ValueSpliterator.forEachRemaining(HashMap.java:1657)
    //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
    //     at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
    //     at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
    //     at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
    //     at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
    //     at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:485)
    //     at com.github.codeteapot.glassfish.haa.Cluster.executionAvailable(Cluster.java:107)
    //     at com.github.codeteapot.glassfish.haa.ClusterManager.lambda$null$2(ClusterManager.java:41)
    //     at java.lang.Iterable.forEach(Iterable.java:75)
    //     at java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1082)
    //     at com.github.codeteapot.glassfish.haa.ClusterManager.lambda$machineAvailable$3(ClusterManager.java:40)
    //     at java.util.Optional.ifPresent(Optional.java:159)
    //     at com.github.codeteapot.glassfish.haa.ClusterManager.machineAvailable(ClusterManager.java:40)
    //     at com.github.codeteapot.ironhoist.EventDispatcher.dispatch(EventDispatcher.java:34)
    //     at java.util.ArrayList.forEach(ArrayList.java:1259)
    //     at java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1082)
    //     at com.github.codeteapot.ironhoist.EventDispatcher.dispatch(EventDispatcher.java:29)
    //     at com.github.codeteapot.ironhoist.PlatformEventQueue.dispatchEvents(PlatformEventQueue.java:47)
    //     at com.github.codeteapot.glassfish.haa.Agent.accept(Agent.java:36)
    //     at com.github.codeteapot.glassfish.haa.AgentLauncher.launch(AgentLauncher.java:53)
    //     at com.github.codeteapot.glassfish.haa.AgentLauncher.main(AgentLauncher.java:45)
  }

  private void dispatch(L listener) {
    try {
      action.accept(listener, event);
    } catch (RuntimeException e) {
      logger.log(SEVERE, "Error occurred while dispatching event", e);
    }
  }
}
