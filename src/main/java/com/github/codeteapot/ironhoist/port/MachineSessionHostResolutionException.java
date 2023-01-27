package com.github.codeteapot.ironhoist.port;

public class MachineSessionHostResolutionException extends Exception {

  private static final long serialVersionUID = 1L;

  public MachineSessionHostResolutionException(String message) {
    super(message);
  }

  public MachineSessionHostResolutionException(Throwable cause) {
    super(cause);
  }

  public MachineSessionHostResolutionException(String message, Throwable cause) {
    super(message, cause);
  }
}
