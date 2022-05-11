package com.applaudostudios.resourceserver.exceptions;

public class CheckoutIsNotReadyException extends RuntimeException {
  private static final long serialVersionUID = -7034897115124457639L;

  public CheckoutIsNotReadyException(final String message) {
    super(message);
  }
}
