package com.github.codeteapot.ironhoist;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

class PlatformContextImpl implements PlatformContext {

  private final Supplier<Stream<? extends Machine>> availableSupplier;
  private final Function<MachineRef, Optional<? extends Machine>> machineMapper;

  PlatformContextImpl(
      Supplier<Stream<? extends Machine>> availableSupplier,
      Function<MachineRef, Optional<? extends Machine>> machineMapper) {
    this.availableSupplier = requireNonNull(availableSupplier);
    this.machineMapper = requireNonNull(machineMapper);
  }

  @Override
  public Stream<? extends Machine> available() {
    return availableSupplier.get();
  }

  @Override
  public Optional<? extends Machine> lookup(MachineRef ref) {
    return machineMapper.apply(ref);
  }
}
