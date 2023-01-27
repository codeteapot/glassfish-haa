package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class MachineMonitor {

  private final ScheduledExecutorService executor;
  private final MachineMonitorRefresher refresher;
  private final long delay;
  private final TimeUnit unit;
  private Future<Void> currentTask;
  private boolean active;

  MachineMonitor(
      ScheduledExecutorService executor,
      MachineMonitorRefresher refresher,
      long delay,
      TimeUnit unit) {
    this.executor = requireNonNull(executor);
    this.refresher = requireNonNull(refresher);
    this.delay = delay;
    this.unit = requireNonNull(unit);
    currentTask = null;
    active = false;
  }

  void activate() {
    if (!active) {
      active = true;
      currentTask = executor.schedule(this::refresh, delay, unit);
    }
  }

  void deactivate() {
    if (active) {
      active = false;
      currentTask = null;
    }
  }

  boolean refreshNow() {
    try {
      if (currentTask != null) {
        currentTask.cancel(true);
      }
      refresh();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Void refresh() throws Exception {
    try {
      refresher.refresh();
      return null;
    } finally {
      if (active) {
        currentTask = executor.schedule(this::refresh, delay, unit);
      }
    }
  }
}
