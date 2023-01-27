package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.MachineRef;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public class ApplicationServerAdministrationBean implements ApplicationServerAdministration {

  private static final long DOMAINS_MONITOR_DELAY_MILLIS = 8000L;

  private final PropertyChangeSupport propertyChangeSupport;
  private final PasswordsCodeGenerator passwordsCodeGenetartor;
  private final AsAdminCommandFactory asAdminCommandFactory;
  private final PasswordsCommandFactory passwordsCommandFactory;
  private final MachineContext context;
  private final ScheduledExecutorService monitorExecutor;
  private final MachineMonitor domainsMonitor;
  private final String username;
  private final String name;
  private final String publicKeyPath;
  private final String adminUser;
  private Set<ApplicationDomainBean> domains;

  public ApplicationServerAdministrationBean(
      MachineContext context,
      ScheduledExecutorService monitorExecutor,
      String asAdminPath,
      String username,
      String name,
      String publicKeyPath,
      String user,
      String passwordsCommandPath) {
    propertyChangeSupport = new PropertyChangeSupport(this);
    passwordsCodeGenetartor = new PasswordsCodeGenerator();
    asAdminCommandFactory = new AsAdminCommandFactory(asAdminPath);
    passwordsCommandFactory = new PasswordsCommandFactory(passwordsCommandPath);
    this.context = requireNonNull(context);
    this.monitorExecutor = requireNonNull(monitorExecutor);
    domainsMonitor = new MachineMonitor(
        monitorExecutor,
        this::domainsMonitorRefresh,
        DOMAINS_MONITOR_DELAY_MILLIS,
        MILLISECONDS);
    domainsMonitor.activate();
    this.username = requireNonNull(username);
    this.name = requireNonNull(name);
    this.publicKeyPath = requireNonNull(publicKeyPath);
    this.adminUser = requireNonNull(user);
    domains = emptySet();
  }

  @Override
  public MachineRef getMachineRef() {
    return context.getRef();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SSHPublicKey getPublicKey() throws ApplicationServerException {
    try (
        MachineSession session = context.getSession(username);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            session.file(publicKeyPath).getInputStream()))) {
      return new SSHPublicKey(reader.readLine());
    } catch (MachineSessionException
        | UnknownUserException
        | MachineSessionHostResolutionException
        | IOException e) {
      throw new ApplicationServerException(e);
    }
  }

  @Override
  public Set<ApplicationDomain> getDomains() {
    domainsMonitor.refreshNow();
    return unmodifiableSet(domains);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void dispose() {
    domainsMonitor.deactivate();
    domains.forEach(ApplicationDomainBean::dispose);
  }

  private void domainsMonitorRefresh() throws Exception {
    try (MachineSession session = context.getSession(username)) {
      Set<ApplicationDomainBean> oldDomains = domains;
      domains = session.execute(asAdminCommandFactory.listDomains())
          .stream()
          .map(entry -> domains.stream()
              .filter(domain -> domain.hasNameValue(entry.getName()))
              .peek(domain -> domain.setAdminHost(entry.getAdminHost()))
              .peek(domain -> domain.setAdminPort(entry.getAdminPort()))
              .peek(domain -> domain.setRunning(entry.isRunning()))
              .peek(domain -> domain.setRestartRequired(entry.isRestartRequired()))
              .findAny()
              .orElseGet(() -> new ApplicationDomainBean(
                  passwordsCodeGenetartor,
                  asAdminCommandFactory,
                  passwordsCommandFactory,
                  context,
                  monitorExecutor,
                  username,
                  adminUser,
                  new ApplicationDomainName(entry.getName()),
                  entry.getAdminHost(),
                  entry.getAdminPort(),
                  entry.isRunning(),
                  entry.isRestartRequired())))
          .collect(toSet());
      oldDomains.stream()
          .filter(domain -> !domains.contains(domain))
          .forEach(ApplicationDomainBean::dispose);
      if (!oldDomains.equals(domains)) {
        propertyChangeSupport.firePropertyChange("domains", oldDomains, domains);
      }
    }
  }
}
