package com.github.codeteapot.ironhoist.machine;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.beans.ConstructorProperties;

public class UnknownUserException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  private final MachineRef machineRef;
  private final String username;
  
  @ConstructorProperties({
    "machineRef",
    "username"
  })
  public UnknownUserException(MachineRef machineRef, String username) {
    super(createMessage(machineRef, username));
    this.machineRef = requireNonNull(machineRef);
    this.username = requireNonNull(username);
  }
  
  public MachineRef getMachineRef() {
    return machineRef;
  }
  
  public String getUsername() {
    return username;
  }
  
  private static String createMessage(MachineRef machineRef, String username) {
    return format("Unknown user %s on machine $s", username, machineRef);
  }
}
