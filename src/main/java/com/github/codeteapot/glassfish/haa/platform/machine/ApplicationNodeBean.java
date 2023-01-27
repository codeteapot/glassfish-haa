package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

class ApplicationNodeBean implements ApplicationNode {

  private final PropertyChangeSupport propertyChangeSupport;
  private final PasswordsCodeGenerator passwordsCodeGenerator;
  private final AsAdminCommandFactory asAdminCommandFactory;
  private final PasswordsCommandFactory passwordsCommandFactory;
  private final MachineContext context;
  private final ApplicationNodeName name;
  private final Set<ApplicationInstanceBean> instances;

  ApplicationNodeBean(
      PasswordsCodeGenerator passwordsCodeGenerator,
      AsAdminCommandFactory asAdminCommandFactory,
      PasswordsCommandFactory passwordsCommandFactory,
      MachineContext context,
      ScheduledExecutorService monitorExecutor,
      ApplicationNodeName name) {
    propertyChangeSupport = new PropertyChangeSupport(this);
    this.passwordsCodeGenerator = requireNonNull(passwordsCodeGenerator);
    this.asAdminCommandFactory = requireNonNull(asAdminCommandFactory);
    this.passwordsCommandFactory = requireNonNull(passwordsCommandFactory);
    this.context = requireNonNull(context);
    this.name = requireNonNull(name);
    instances = new HashSet<>();
  }

  @Override
  public ApplicationNodeName getName() {
    return name;
  }

  @Override
  public Set<ApplicationInstance> getInstances() {
    return unmodifiableSet(instances);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof ApplicationNodeBean) {
      ApplicationNodeBean node = (ApplicationNodeBean) obj;
      return name.equals(node.name);
    }
    return false;
  }

  boolean hasNameValue(String nameValue) {
    return name.hasValue(nameValue);
  }
}
