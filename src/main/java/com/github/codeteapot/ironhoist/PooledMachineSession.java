package com.github.codeteapot.ironhoist;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import com.github.codeteapot.ironhoist.session.MachineSessionFile;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;

class PooledMachineSession implements MachineSession {

  private final MachineSession managedSession;
  private final ScheduledExecutorService releaseExecutor;
  private final Consumer<PooledMachineSession> removeAction;
  private final String username;
  private final Duration idleTimeout;
  private ScheduledFuture<Void> releaseFuture;

  PooledMachineSession(
      MachineSession managedSession,
      ScheduledExecutorService releaseExecutor,
      Consumer<PooledMachineSession> removeAction,
      String username,
      Duration idleTimeout) {
    this.managedSession = requireNonNull(managedSession);
    this.releaseExecutor = requireNonNull(releaseExecutor);
    this.removeAction = requireNonNull(removeAction);
    this.username = requireNonNull(username);
    this.idleTimeout = requireNonNull(idleTimeout);
    releaseFuture = null;
  }

  @Override
  public <R> R execute(MachineCommand<R> command) throws MachineSessionException {
    return managedSession.execute(command);
  }

  @Override
  public MachineSessionFile file(String path) throws MachineSessionException {
    return managedSession.file(path);
  }

  @Override
  public void close() throws IOException {
    releaseFuture = releaseExecutor.schedule(
        this::removeAndClose,
        idleTimeout.toMillis(),
        MILLISECONDS);
  }

  boolean available(String username) {
    return releaseFuture != null && username.equals(this.username);
  }

  void acquire() {
    releaseFuture.cancel(true);
    releaseFuture = null;
  }

  void releaseNow() {
    try {
      releaseFuture.cancel(true);
      removeAndClose();
    } catch (IOException e) {
      // Nothing to be done
    }
  }

  private Void removeAndClose() throws IOException {
    removeAction.accept(this);
    managedSession.close();
    return null;
  }
}
