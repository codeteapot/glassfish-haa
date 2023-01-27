package com.github.codeteapot.ironhoist;

import static java.lang.Thread.sleep;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.io.IOException;
import java.time.Duration;

class MachineReadyChecker {

  private static final long DEFAULT_INITIAL_DELAY_MILLIS = 0;
  private static final long DEFAULT_RETRY_DELAY_MILLIS = 800;

  MachineReadyChecker() {}

  void awaitReady(MachineContext context, MachineReadyCheck readyCheck)
      throws UnknownUserException, InterruptedException {
    sleep(readyCheck.getInitialDelay()
        .map(Duration::toMillis)
        .orElse(DEFAULT_INITIAL_DELAY_MILLIS));
    while (!checked(context, readyCheck)) {
      sleep(readyCheck.getRetryDelay()
          .map(Duration::toMillis)
          .orElse(DEFAULT_RETRY_DELAY_MILLIS));
    }
  }

  private boolean checked(MachineContext context, MachineReadyCheck readyCheck)
      throws UnknownUserException, InterruptedException {
    try (MachineSession session = context.getSession(readyCheck.getUsername())) {
      return session.execute(readyCheck.getCommand().orElseGet(UnixTrueCommand::new));
    } catch (MachineSessionException | MachineSessionHostResolutionException | IOException e) {
      return false;
    }
  }
}
