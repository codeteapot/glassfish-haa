package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import java.time.Duration;
import java.util.Optional;

public interface MachineReadyCheck {

  public String getUsername();

  public Optional<MachineCommand<Boolean>> getCommand();

  public Optional<Duration> getInitialDelay();
  
  public Optional<Duration> getRetryDelay();
}
