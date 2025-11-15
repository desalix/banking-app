package com.bankapp.service.exception;

/**
 * Thrown when a withdrawal or transfer is attempted
 * but the account has an insufficient balance.
 */
public class InsufficientFundsException extends Exception {

  public InsufficientFundsException(String message) {
    super(message);
  }

  public InsufficientFundsException(String message, Throwable cause) {
    super(message, cause);
  }
}
