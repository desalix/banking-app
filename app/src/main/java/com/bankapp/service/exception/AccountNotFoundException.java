package com.bankapp.service.exception;

/**
 * Thrown when an operation is attempted on an account
 * that does not exist.
 */
public class AccountNotFoundException extends Exception {

  public AccountNotFoundException(String message) {
    super(message);
  }

  public AccountNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
