package com.applaudostudios.resourceserver.exceptions;

import java.util.List;
import javax.validation.ConstraintViolationException;
import org.apache.commons.logging.Log;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  public GlobalExceptionHandler() {
  }

  private ErrorMessage message(final HttpStatus httpStatus, final Exception ex) {
    String message = ex.getMessage();
    return new ErrorMessage(message, httpStatus.value());
  }

  @ExceptionHandler({DataIntegrityViolationException.class})
  protected final ResponseEntity<Object> handleBadRequest(final RuntimeException ex, final WebRequest request) {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("The DB can't add two records when violating an unique constraint");
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.BAD_REQUEST, new UniqueConstraintViolation("Violating the unique constraint when persisting the DB")), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({ResourceNotFoundException.class})
  protected ResponseEntity<Object> handleResourceNotFound(final ResourceNotFoundException ex, final WebRequest request) {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug(ex.getResource() + " not found");
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.NOT_FOUND, new Exception(ex.getMessage())), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler({CheckoutIsNotReadyException.class})
  protected ResponseEntity<Object> handleIncompleteCheckout(final CheckoutIsNotReadyException ex, final WebRequest request) {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug(ex.getMessage());
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.BAD_REQUEST, new Exception(ex.getMessage())), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({UserAlreadyRegistered.class})
  protected ResponseEntity<Object> handleResourceNotFound(final UserAlreadyRegistered ex, final WebRequest request) {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug(ex.getMessage());
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.BAD_REQUEST, new Exception(ex.getMessage())), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  protected ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
    if (this.logger.isDebugEnabled()) {
      Log var10000 = this.logger;
      String[] var10001 = ex.getMessage().split(":");
      var10000.debug("CONSTRAINT VALIDATION FAILURE " + var10001[1]);
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.BAD_REQUEST, new Exception(ex.getMessage().split(":")[1].trim())), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    String errorMessage = ((FieldError)ex.getBindingResult().getFieldErrors().get(0)).getDefaultMessage();
    List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("VALIDATION ERROR : " + validationList);
    }

    return this.handleExceptionInternal(ex, this.message(HttpStatus.BAD_REQUEST, new Exception(errorMessage)), headers, status, request);
  }
}