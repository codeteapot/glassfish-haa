package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.event.MachineAvailableEvent;
import com.github.codeteapot.ironhoist.event.MachineLostEvent;

public interface PlatformEventSource {

  void fireEvent(MachineAvailableEvent event);

  void fireEvent(MachineLostEvent event);
}
