package com.github.codeteapot.glassfish.haa.platform.machine;

import com.github.codeteapot.ironhoist.machine.MachineRef;
import java.beans.PropertyChangeListener;
import java.util.Set;

public interface ApplicationServerAdministration {

  MachineRef getMachineRef();

  String getName();

  SSHPublicKey getPublicKey() throws ApplicationServerException;

  Set<ApplicationDomain> getDomains();

  void addPropertyChangeListener(PropertyChangeListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);
}
