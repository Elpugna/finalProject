package com.applaudostudios.resourceserver.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -7034897115124457333L;
  private final String resource;

  public ResourceNotFoundException(final String message, final String resource) {
    super(message);
    this.resource = resource;
  }

  public String getResource() {
    return this.resource;
  }
}
