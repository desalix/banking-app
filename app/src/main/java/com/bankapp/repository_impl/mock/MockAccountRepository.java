package com.bankapp.repository_impl.mock;

import com.bankapp.model.Account;
import com.bankapp.model.Customer;
import com.bankapp.repository.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * A "mock" implementation of the AccountRepository for development and testing.
 * It stores data in memory in a HashMap and simulates database behavior.
 */
public class MockAccountRepository implements AccountRepository {

  private final Map<Long, Account> accountStore = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(0L);

  @Override
  public Account save(Account account) {
    if (account == null) {
      throw new IllegalArgumentException("Account cannot be null");
    }

    if (account.getId() == null) {
      // New account (INSERT)
      long newId = idGenerator.incrementAndGet();
      account.setId(newId);
      accountStore.put(newId, account);
      return account;
    } else {
      // Existing account (UPDATE)
      accountStore.put(account.getId(), account);
      return account;
    }
  }

  @Override
  public Optional<Account> findById(Long id) {
    return Optional.ofNullable(accountStore.get(id));
  }

  @Override
  public Optional<Account> findByAccountNumber(String accountNumber) {
    if (accountNumber == null) {
      return Optional.empty();
    }

    // Simulate a database query by looping through the map values
    return accountStore.values().stream()
        .filter(account -> accountNumber.equals(account.getAccountNumber()))
        .findFirst();
  }

  @Override
  public Set<Account> findAllByCustomer(Customer customer) {
    if (customer == null || customer.getId() == null) {
      return Set.of(); // Return an empty, immutable set
    }

    // Simulate the "WHERE customer_id = ?" query
    return accountStore.values().stream()
        .filter(account -> customer.equals(account.getCustomer()))
        .collect(Collectors.toSet());
  }
}
