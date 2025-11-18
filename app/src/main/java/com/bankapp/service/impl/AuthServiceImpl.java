package com.bankapp.service.impl;

import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.service.AuthService;
import com.bankapp.service.exception.AuthException;
import com.bankapp.service.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceImpl implements AuthService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * This is Dependency Injection. We "inject" the repository
   * this service needs to function.
   *
   * @param customerRepository The data access repository (e.g.,
   *                           MockCustomerRepository)
   */
  public AuthServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    this.passwordEncoder = new BCryptPasswordEncoder(); // Use the standard BCrypt hasher
  }

  @Override
  public Customer login(String username, String password) throws AuthException {
    // 1. Find the user by their username
    Customer customer = customerRepository.findByUsername(username)
        .orElseThrow(() -> new AuthException("Invalid username or password"));

    // 2. Check if the plain-text password matches the stored hash
    if (passwordEncoder.matches(password, customer.getPasswordHash())) {
      // 3. Success! Return the user.
      return customer;
    } else {
      // 4. Fail. Throw the same error for security (don't say "wrong password").
      throw new AuthException("Invalid username or password");
    }
  }

  @Override
  public Customer register(String username, String password, String firstName, String lastName, String email)
      throws UserAlreadyExistsException {

    // 1. === YOUR DEFENSIVE CODE (Username) ===
    // Check if username is already taken
    if (customerRepository.findByUsername(username).isPresent()) {
      throw new UserAlreadyExistsException("Username is already taken: " + username);
    }

    // 2. === DEFENSIVE CODE (Email) ===
    // We also need to check if the email is taken.
    // This will require adding a 'findByEmail' method to our repository.
    if (customerRepository.findByEmail(email).isPresent()) {
      throw new UserAlreadyExistsException("Email is already taken: " + email);
    }

    // 3. Hash the password
    String passwordHash = passwordEncoder.encode(password);

    // 4. Create the new customer
    Customer newCustomer = new Customer(username, passwordHash, firstName, lastName, email);

    // 5. Save and return the new customer
    return customerRepository.save(newCustomer);
  }
}
