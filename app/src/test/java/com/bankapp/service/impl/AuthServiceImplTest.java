package com.bankapp.service.impl;

import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.repository_impl.mock.MockCustomerRepository;
import com.bankapp.service.AuthService;
import com.bankapp.service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceImplTest {

  private CustomerRepository customerRepository;
  private AuthService authService;

  // This setup runs before each @Test method
  @BeforeEach
  void setUp() {
    // 1. Create a fresh mock repository for each test
    customerRepository = new MockCustomerRepository();

    // 2. Create the service we want to test, injecting the mock
    authService = new AuthServiceImpl(customerRepository);
  }

  @Test
  void testRegisterUserSuccessfully() throws UserAlreadyExistsException {
    // --- Arrange ---
    String username = "johndoe";
    String password = "password123";

    // --- Act ---
    Customer customer = authService.register(username, password, "John", "Doe", "john@example.com");

    // --- Assert ---
    assertNotNull(customer);
    assertNotNull(customer.getId());
    assertEquals(username, customer.getUsername());

    // Crtitical: Verify the password was hashed
    assertNotEquals(password, customer.getPasswordHash());

    // Verify it was actually saved in our mock repo
    assertTrue(customerRepository.findByUsername(username).isPresent());
  }

  @Test
  void testRegisterUserWithDuplicateUsername() throws UserAlreadyExistsException {
    // --- Arrange ---
    // 1. Create the first user
    authService.register("johndoe", "password123", "John", "Doe", "john@example.com");

    // --- Act & Assert ---
    // 2. Use assertThrows to verify that the *second* attempt fails
    UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
      // This code is expected to throw the exception
      authService.register("johndoe", "newpass", "Jane", "Smith", "jane@example.com");
    });

    // 3. (Optional) Check the error message
    assertTrue(exception.getMessage().contains("Username is already taken"));
  }
}
