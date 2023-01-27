package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.Optional;
import java.util.stream.Stream;

public interface PlatformContext {

  Stream<? extends Machine> available();

  Optional<? extends Machine> lookup(MachineRef ref);
}
