package com.bankapp.repository;

import com.bankapp.model.Customer;
import java.util.List;
import java.util.Optional;

/**
 * Interface (contract) for data access operations related to Customers.
 * This defines *what* we can do, but not *how* we do it.
 */
public interface CustomerRepository {

  /**
   * Saves a Customer. If the customer has no ID, it's an 'insert'.
   * If it has an ID, it's an 'update'.
   *
   * @param customer The customer object to save.
   * @return The saved customer (often with a newly generated ID).
   */
  Customer save(Customer customer);

  /**
   * Finds a customer by their unique database ID.
   *
   * @param id The ID of the customer.
   * @return An Optional containing the customer if found, or an empty Optional if
   *         not.
   */
  Optional<Customer> findById(Long id);

  /**
   * Finds a customer by their unique username.
   *
   * @param username The username to search for.
   * @return An Optional containing the customer if found, or an empty Optional if
   *         not.
   */
  Optional<Customer> findByUsername(String username);

  /**
   * Finds all customers in the database.
   * (Useful for admin panels, etc.)
   *
   * @return A List of all Customer objects.
   */
  List<Customer> findAll();

  /**
   * Deletes a customer from the database.
   *
   * @param customer The customer object to delete.
   */
  void delete(Customer customer);

  /**
   * Finds a customer by their unique email.
   *
   * @param email The email to search for.
   * @return An Optional containing the customer if found, or an empty Optional if
   *         not.
   */
  Optional<Customer> findByEmail(String email);
}
