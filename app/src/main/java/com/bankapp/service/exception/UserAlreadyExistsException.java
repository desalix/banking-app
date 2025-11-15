package com.bankapp.service.exception;

/**
 * Thrown when a user tries to register with a username
 * or email that is already taken.
 */
public class UserAlreadyExistsException extends Exception {

  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
