package com.bankapp.service.exception;

/**
 * Thrown when a deposit, withdrawal, or transfer is
 * attempted with an invalid amount (e.g., zero or negative).
 */
public class InvalidAmountException extends Exception {

  public InvalidAmountException(String message) {
    super(message);
  }

  public InvalidAmountException(String message, Throwable cause) {
    super(message, cause);
  }
}
