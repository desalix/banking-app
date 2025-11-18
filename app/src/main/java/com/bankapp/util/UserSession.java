package com.bankapp.util;

import com.bankapp.model.Customer;

public class UserSession {

  private static UserSession instance;

  private Customer customer;

  private UserSession() {
    // Private constructor to prevent instantiation
  }

  /**
   * Returns the single instance of UserSession.
   */
  public static synchronized UserSession getInstance() {
    if (instance == null) {
      instance = new UserSession();
    }
    return instance;
  }

  public void login(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void logout() {
    this.customer = null;
  }

  public boolean isUserLoggedIn() {
    return this.customer != null;
  }
}
