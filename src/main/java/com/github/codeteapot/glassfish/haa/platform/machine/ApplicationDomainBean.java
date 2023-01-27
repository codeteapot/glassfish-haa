package com.github.codeteapot.glassfish.haa.platform.machine;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toSet;

import com.github.codeteapot.ironhoist.machine.MachineContext;
import com.github.codeteapot.ironhoist.machine.UnknownUserException;
import com.github.codeteapot.ironhoist.port.MachineSessionHostResolutionException;
import com.github.codeteapot.ironhoist.session.MachineSession;
import com.github.codeteapot.ironhoist.session.MachineSessionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

class ApplicationDomainBean implements ApplicationDomain {

  private static final long CLUSTERS_MONITOR_DELAY_MILLIS = 8000L;
  private static final long NODES_MONITOR_DELAY_MILLIS = 8000L;

  private final PropertyChangeSupport propertyChangeSupport;
  private final PasswordsCodeGenerator passwordsCodeGenerator;
  private final AsAdminCommandFactory asAdminCommandFactory;
  private final PasswordsCommandFactory passwordsCommandFactory;
  private final MachineContext context;
  private final ScheduledExecutorService monitorExecutor;
  private final MachineMonitor clustersMonitor;
  private final MachineMonitor nodesMonitor;
  private final String username;
  private final String adminUser;
  private final ApplicationDomainName name;
  private String adminHost;
  private int adminPort;
  private boolean running;
  private boolean restartRequired;
  private Set<ApplicationClusterBean> clusters;
  private Set<ApplicationNodeBean> nodes;

  ApplicationDomainBean(
      PasswordsCodeGenerator passwordsCodeGenetator,
      AsAdminCommandFactory asAdminCommandFactory,
      PasswordsCommandFactory passwordsCommandFactory,
      MachineContext context,
      ScheduledExecutorService monitorExecutor,
      String username,
      String adminUser,
      ApplicationDomainName name,
      String adminHost,
      int adminPort,
      boolean running,
      boolean restartRequired) {
    propertyChangeSupport = new PropertyChangeSupport(this);
    this.passwordsCodeGenerator = requireNonNull(passwordsCodeGenetator);
    this.asAdminCommandFactory = requireNonNull(asAdminCommandFactory);
    this.passwordsCommandFactory = requireNonNull(passwordsCommandFactory);
    this.context = requireNonNull(context);
    this.monitorExecutor = requireNonNull(monitorExecutor);
    clustersMonitor = new MachineMonitor(
        monitorExecutor,
        this::clustersMonitorRefresh,
        CLUSTERS_MONITOR_DELAY_MILLIS,
        MILLISECONDS);
    nodesMonitor = new MachineMonitor(
        monitorExecutor,
        this::nodesMonitorRefresh,
        NODES_MONITOR_DELAY_MILLIS,
        MILLISECONDS);
    this.username = requireNonNull(username);
    this.adminUser = requireNonNull(adminUser);
    this.name = requireNonNull(name);
    this.adminHost = requireNonNull(adminHost);
    this.adminPort = adminPort;
    this.running = running;
    this.restartRequired = restartRequired;
    clusters = emptySet();
    nodes = emptySet();
    if (running) {
      clustersMonitor.activate();
      nodesMonitor.activate();
    }
  }

  @Override
  public ApplicationDomainName getName() {
    return name;
  }

  @Override
  public boolean isRunning() {
    return running;
  }

  @Override
  public boolean isRestartRequired() {
    return restartRequired;
  }

  @Override
  public Set<ApplicationCluster> getClusters() {
    clustersMonitor.refreshNow();
    return unmodifiableSet(clusters);
  }

  @Override
  public Set<ApplicationNode> getNodes() {
    nodesMonitor.refreshNow();
    return unmodifiableSet(nodes);
  }

  @Override
  public void createNode(ApplicationNodeName name, ApplicationNodeRequirements requirements)
      throws ApplicationDomainException {
    try (MachineSession session = context.getSession(username)) {
      String passwordsCode = passwordsCodeGenerator.generate();
      try {
        session.execute(asAdminCommandFactory.createNodeSSH(
            new AsAdminTarget(
                adminHost,
                adminPort,
                adminUser,
                session.execute(passwordsCommandFactory.getPasswords(passwordsCode))),
            name.getValue(),
            requirements.getNodeHost(),
            requirements.getInstallDir().orElse(null),
            requirements.getNodeDir().orElse(null),
            null,
            null,
            null,
            null));
      } finally {
        session.execute(passwordsCommandFactory.removePasswords(passwordsCode));
      }
    } catch (UnknownUserException
        | MachineSessionHostResolutionException
        | MachineSessionException
        | IOException e) {
      throw new ApplicationDomainException(e);
    }
  }

  @Override
  public void deleteNode(ApplicationNodeName name) throws ApplicationDomainException {
    try (MachineSession session = context.getSession(username)) {
      String passwordsCode = passwordsCodeGenerator.generate();
      try {
        session.execute(asAdminCommandFactory.deleteNodeSSH(
            new AsAdminTarget(
                adminHost,
                adminPort,
                adminUser,
                session.execute(passwordsCommandFactory.getPasswords(passwordsCode))),
            name.getValue()));
      } finally {
        session.execute(passwordsCommandFactory.removePasswords(passwordsCode));
      }
    } catch (UnknownUserException
        | MachineSessionHostResolutionException
        | MachineSessionException
        | IOException e) {
      throw new ApplicationDomainException(e);
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
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
    if (obj instanceof ApplicationDomainBean) {
      ApplicationDomainBean domain = (ApplicationDomainBean) obj;
      return name.equals(domain.name);
    }
    return false;
  }

  boolean hasNameValue(String nameValue) {
    return name.hasValue(nameValue);
  }

  void setAdminHost(String adminHost) {
    this.adminHost = requireNonNull(adminHost);
  }

  void setAdminPort(int adminPort) {
    this.adminPort = requireNonNull(adminPort);
  }

  void setRunning(boolean running) {
    boolean oldRunning = this.running;
    this.running = running;
    if (running) {
      clustersMonitor.activate();
      nodesMonitor.activate();
    } else {
      clustersMonitor.deactivate();
      nodesMonitor.deactivate();
    }
    if (oldRunning != running) {
      propertyChangeSupport.firePropertyChange("running", oldRunning, running);
    }
  }

  void setRestartRequired(boolean restartRequired) {
    boolean oldRestartRequired = this.restartRequired;
    this.restartRequired = restartRequired;
    if (oldRestartRequired != restartRequired) {
      propertyChangeSupport.firePropertyChange("restartRequired", oldRestartRequired,
          restartRequired);
    }
  }

  void dispose() {
    clustersMonitor.deactivate();
    nodesMonitor.deactivate();
  }

  private void clustersMonitorRefresh() throws Exception {
    try (MachineSession session = context.getSession(username)) {
      String passwordsCode = passwordsCodeGenerator.generate();
      Set<ApplicationClusterBean> oldClusters = clusters;
      try {
        clusters = session.execute(asAdminCommandFactory.listClusters(new AsAdminTarget(
            adminHost,
            adminPort,
            adminUser,
            session.execute(passwordsCommandFactory.getPasswords(passwordsCode)))))
            .stream()
            .map(entry -> clusters.stream()
                .filter(cluster -> cluster.hasNameValue(entry.getName()))
                .findAny()
                .orElseGet(() -> new ApplicationClusterBean(new ApplicationClusterName(
                    entry.getName()))))
            .collect(toSet());
      } finally {
        session.execute(passwordsCommandFactory.removePasswords(passwordsCode));
      }
      if (!oldClusters.equals(clusters)) {
        propertyChangeSupport.firePropertyChange("clusters", oldClusters, clusters);
      }
    }
  }

  private void nodesMonitorRefresh() throws Exception {
    try (MachineSession session = context.getSession(username)) {
      String passwordsCode = passwordsCodeGenerator.generate();
      Set<ApplicationNodeBean> oldNodes = nodes;
      try {
        nodes = session.execute(asAdminCommandFactory.listNodesSSH(new AsAdminTarget(
            adminHost,
            adminPort,
            adminUser,
            session.execute(passwordsCommandFactory.getPasswords(passwordsCode)))))
            .stream()
            .map(entry -> nodes.stream()
                .filter(node -> node.hasNameValue(entry.getName()))
                .findAny()
                .orElseGet(() -> new ApplicationNodeBean(
                    passwordsCodeGenerator,
                    asAdminCommandFactory,
                    passwordsCommandFactory,
                    context,
                    monitorExecutor,
                    new ApplicationNodeName(entry.getName()))))
            .collect(toSet());
      } finally {
        session.execute(passwordsCommandFactory.removePasswords(passwordsCode));
      }
      if (!oldNodes.equals(nodes)) {
        propertyChangeSupport.firePropertyChange("nodes", oldNodes, nodes);
      }
    }
  }
}
