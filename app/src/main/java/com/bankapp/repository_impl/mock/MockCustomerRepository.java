package com.bankapp.repository_impl.mock;

import com.bankapp.model.Customer;
import com.bankapp.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A "mock" implementation of the CustomerRepository for development and
 * testing.
 * It stores data in memory in a HashMap and simulates database behavior.
 */
public class MockCustomerRepository implements CustomerRepository {

  // 1. Use ConcurrentHashMap for thread-safety, a good practice.
  private final Map<Long, Customer> customerStore = new ConcurrentHashMap<>();

  // 2. Use AtomicLong to safely generate new, unique IDs, just like a database.
  private final AtomicLong idGenerator = new AtomicLong(0L);

  @Override
  public Customer save(Customer customer) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }

    if (customer.getId() == null) {
      // This is a new customer (an INSERT)
      long newId = idGenerator.incrementAndGet();
      customer.setId(newId);
      customerStore.put(newId, customer);
      return customer;
    } else {
      // This is an existing customer (an UPDATE)
      customerStore.put(customer.getId(), customer);
      return customer;
    }
  }

  @Override
  public Optional<Customer> findById(Long id) {
    // Optional.ofNullable handles the case where the customer is not found (returns
    // Optional.empty())
    return Optional.ofNullable(customerStore.get(id));
  }

  @Override
  public Optional<Customer> findByUsername(String username) {
    if (username == null) {
      return Optional.empty();
    }

    // This simulates a database query by looping through the map values
    return customerStore.values().stream()
        .filter(customer -> username.equals(customer.getUsername()))
        .findFirst();
  }

  @Override
  public List<Customer> findAll() {
    // Return a new list to prevent modification of the underlying store
    return new ArrayList<>(customerStore.values());
  }

  @Override
  public void delete(Customer customer) {
    if (customer != null && customer.getId() != null) {
      customerStore.remove(customer.getId());
    }
  }

  @Override
  public Optional<Customer> findByEmail(String email) {
    if (email == null) {
      return Optional.empty();
    }
    // This simulates a database query by looping through the map values
    return customerStore.values().stream()
        .filter(customer -> email.equals(customer.getEmail()))
        .findFirst();
  }
}
