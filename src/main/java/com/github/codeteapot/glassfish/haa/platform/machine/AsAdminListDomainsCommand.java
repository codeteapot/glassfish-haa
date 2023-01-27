package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.session.MachineCommand;
import com.github.codeteapot.ironhoist.session.MachineCommandExecution;
import com.github.codeteapot.ironhoist.session.MachineCommandExecutionContext;
import java.util.Set;

class AsAdminListDomainsCommand implements MachineCommand<Set<AsAdminDomainEntry>> {

  private final String asAdminPath;

  AsAdminListDomainsCommand(String asAdminPath) {
    this.asAdminPath = requireNonNull(asAdminPath);
  }

  @Override
  public String getSentence() {
    return new StringBuilder()
        .append(asAdminPath)
        .append(" list-domains")
        .append(" --terse=true")
        .append(" --header=false")
        .append(" --long=true")
        .toString();
  }

  @Override
  public MachineCommandExecution<Set<AsAdminDomainEntry>> getExecution(
      MachineCommandExecutionContext context) {
    return new AsAdminListDomainsCommandExecution(context);
  }
}
