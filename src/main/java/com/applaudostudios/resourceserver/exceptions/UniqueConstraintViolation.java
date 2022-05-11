package com.applaudostudios.resourceserver.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class UniqueConstraintViolation extends DataIntegrityViolationException {
  private static final long serialVersionUID = -7034897535124457639L;

  public UniqueConstraintViolation(final String message) {
    super(message);
  }
}
