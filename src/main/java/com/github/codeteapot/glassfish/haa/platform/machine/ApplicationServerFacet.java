package com.github.codeteapot.glassfish.haa.platform.machine;

import com.github.codeteapot.ironhoist.machine.MachineFacet;
import java.util.Optional;
import java.util.stream.Stream;

public interface ApplicationServerFacet extends MachineFacet {

  Optional<ApplicationServerAdministration> getAdministration();

  Optional<ApplicationServerExecution> getExecution();

  default Stream<ApplicationServerAdministration> filterAdministration() {
    return getAdministration().map(Stream::of).orElseGet(Stream::empty);
  }

  default Stream<ApplicationServerExecution> filterExecution() {
    return getExecution().map(Stream::of).orElseGet(Stream::empty);
  }
}
