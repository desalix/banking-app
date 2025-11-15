package com.bankapp.service;

import com.bankapp.model.Customer;
import com.bankapp.service.exception.AuthException;
import com.bankapp.service.exception.UserAlreadyExistsException;

/**
 * Interface (contract) for the Authentication Service.
 * This defines the business logic for logging in and registering users.
 */
public interface AuthService {

  /**
   * Attempts to log in a user with their credentials.
   *
   * @param username The user's username.
   * @param password The user's plain-text password.
   * @return The authenticated Customer object.
   * @throws AuthException if the username is not found or the password is
   *                       incorrect.
   */
  Customer login(String username, String password) throws AuthException;

  /**
   * Registers a new user in the system.
   *
   * @param username  The desired username.
   * @param password  The user's plain-text password.
   * @param firstName The user's first name.
   * @param lastName  The user's last name.
   * @param email     The user's email address.
   * @return The newly created Customer object.
   * @throws UserAlreadyExistsException if the username or email is already taken.
   */
  Customer register(String username, String password, String firstName, String lastName, String email)
      throws UserAlreadyExistsException;

}
