package com.bankapp.repository;

import com.bankapp.model.Account;
import com.bankapp.model.Customer;
import java.util.Optional;
import java.util.Set;

/**
 * Interface (contract) for data access operations related to Accounts.
 */
public interface AccountRepository {

  /**
   * Saves a new or existing Account to the database.
   *
   * @param account The account object to save.
   * @return The saved account.
   */
  Account save(Account account);

  /**
   * Finds an account by its unique database ID.
   *
   * @param id The ID of the account.
   * @return An Optional containing the account if found, or an empty Optional if
   *         not.
   */
  Optional<Account> findById(Long id);

  /**
   * Finds an account by its unique, human-readable account number.
   *
   * @param accountNumber The account number (e.g., "123-456-789").
   * @return An Optional containing the account if found, or an empty Optional if
   *         not.
   */
  Optional<Account> findByAccountNumber(String accountNumber);

  /**
   * Finds all accounts belonging to a specific customer.
   *
   * @param customer The customer object.
   * @return A Set of all accounts owned by that customer.
   */
  Set<Account> findAllByCustomer(Customer customer);

}
