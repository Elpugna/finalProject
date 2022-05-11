package com.applaudostudios.resourceserver.exceptions;

import lombok.Generated;

public class ErrorMessage {
  private String message;
  private int status;

  @Generated
  public ErrorMessage(final String message, final int status) {
    this.message = message;
    this.status = status;
  }

  @Generated
  public String getMessage() {
    return this.message;
  }

  @Generated
  public int getStatus() {
    return this.status;
  }
}
