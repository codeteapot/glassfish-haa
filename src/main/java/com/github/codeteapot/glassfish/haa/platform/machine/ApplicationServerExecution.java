package com.github.codeteapot.glassfish.haa.platform.machine;

import com.github.codeteapot.ironhoist.machine.MachineRef;

public interface ApplicationServerExecution {

  MachineRef getMachineRef();

  String getGroup();

  ApplicationNodeRequirements getNodeRequirements() throws ApplicationServerException;

  boolean addAuthorizedKey(SSHPublicKey publicKey) throws ApplicationServerException;

  boolean removeAuthorizedKey(SSHPublicKey publicKey) throws ApplicationServerException;
}
