package com.applaudostudios.resourceserver.exceptions;

public class UserAlreadyRegistered extends RuntimeException {
  private static final long serialVersionUID = -788888115124457639L;

  public UserAlreadyRegistered(final String message) {
    super(message);
  }
}
