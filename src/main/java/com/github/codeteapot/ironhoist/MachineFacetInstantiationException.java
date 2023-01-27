package com.github.codeteapot.ironhoist;

public class MachineFacetInstantiationException extends Exception {

  private static final long serialVersionUID = 1L;

  public MachineFacetInstantiationException(String message) {
    super(message);
  }

  public MachineFacetInstantiationException(Throwable cause) {
    super(cause);
  }

  public MachineFacetInstantiationException(String message, Throwable cause) {
    super(message, cause);
  }
}
