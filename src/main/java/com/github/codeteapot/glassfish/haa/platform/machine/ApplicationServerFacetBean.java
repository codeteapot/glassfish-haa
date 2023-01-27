package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import com.github.codeteapot.ironhoist.machine.Disposable;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.util.Optional;

public class ApplicationServerFacetBean implements ApplicationServerFacet, Disposable {

  private final MachineRef ref;
  private final ApplicationServerAdministrationBean administration;
  private final ApplicationServerExecutionBean execution;

  public ApplicationServerFacetBean(
      MachineRef ref,
      ApplicationServerAdministrationBean administration,
      ApplicationServerExecutionBean execution) {
    this.ref = requireNonNull(ref);
    this.administration = administration;
    this.execution = execution;
  }

  @Override
  public MachineRef getRef() {
    return ref;
  }

  @Override
  public Optional<ApplicationServerAdministration> getAdministration() {
    return ofNullable(administration);
  }

  @Override
  public Optional<ApplicationServerExecution> getExecution() {
    return ofNullable(execution);
  }

  @Override
  public void dispose() {
    ofNullable(administration).ifPresent(ApplicationServerAdministrationBean::dispose);
  }
}
