package com.github.codeteapot.glassfish.haa.platform.machine;

import java.beans.PropertyChangeListener;
import java.util.Set;

public interface ApplicationDomain {

  ApplicationDomainName getName();

  boolean isRunning();

  boolean isRestartRequired();

  Set<ApplicationCluster> getClusters();

  Set<ApplicationNode> getNodes();

  void createNode(ApplicationNodeName name, ApplicationNodeRequirements requirements)
      throws ApplicationDomainException;

  void deleteNode(ApplicationNodeName name) throws ApplicationDomainException;

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);
}
