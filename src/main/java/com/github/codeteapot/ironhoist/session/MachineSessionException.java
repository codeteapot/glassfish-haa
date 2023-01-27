package com.github.codeteapot.ironhoist.session;

public class MachineSessionException extends Exception {

  private static final long serialVersionUID = 1L;

  public MachineSessionException(String message) {
    super(message);
  }

  public MachineSessionException(Throwable cause) {
    super(cause);
  }
}
