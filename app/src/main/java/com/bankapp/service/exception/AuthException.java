package com.bankapp.service.exception;

/**
 * Thrown when an authentication attempt fails
 * (e.g., bad username or incorrect password).
 */
public class AuthException extends Exception {

  public AuthException(String message) {
    super(message);
  }

  public AuthException(String message, Throwable cause) {
    super(message, cause);
  }
}
