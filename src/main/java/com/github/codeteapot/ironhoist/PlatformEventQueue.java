package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.event.MachineAvailableEvent;
import com.github.codeteapot.ironhoist.event.MachineLostEvent;
import com.github.codeteapot.ironhoist.event.PlatformListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlatformEventQueue implements PlatformEventSource {

  private final BlockingQueue<EventDispatcher<?, ?>> dispatcherQueue;
  private final List<PlatformListener> listeners;

  public PlatformEventQueue() {
    dispatcherQueue = new LinkedBlockingQueue<>();
    listeners = new ArrayList<>();
  }

  @Override
  public void fireEvent(MachineAvailableEvent event) {
    dispatcherQueue.offer(new EventDispatcher<>(
        event,
        listeners,
        PlatformListener::machineAvailable));
  }

  @Override
  public void fireEvent(MachineLostEvent event) {
    dispatcherQueue.offer(new EventDispatcher<>(
        event,
        listeners,
        PlatformListener::machineLost));
  }

  public void addListener(PlatformListener listener) {
    listeners.add(listener);
  }

  public void removeListener(PlatformListener listener) {
    listeners.remove(listener);
  }

  public void dispatchEvents() throws InterruptedException {
    while (true) {
      dispatcherQueue.take().dispatch();
    }
  }
}
