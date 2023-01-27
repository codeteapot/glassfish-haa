package com.github.codeteapot.ironhoist;

import com.github.codeteapot.ironhoist.machine.MachineFacet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Machine {

  <F extends MachineFacet> Optional<F> getFacet(Class<F> type);
  
  static <F extends MachineFacet> Function<Machine, Optional<F>> facetGet(Class<F> type) {
    return machine -> machine.getFacet(type);
  }
  
  static <F extends MachineFacet> Function<Machine, Stream<F>> facetFilter(Class<F> type) {
    return machine -> machine.getFacet(type).map(Stream::of).orElseGet(Stream::empty);
  }
}
