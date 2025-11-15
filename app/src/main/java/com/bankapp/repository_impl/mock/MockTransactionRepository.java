package com.bankapp.repository_impl.mock;

import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import com.bankapp.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * A "mock" implementation of the TransactionRepository for development and
 * testing.
 * It stores data in memory in a List and simulates database behavior.
 */
public class MockTransactionRepository implements TransactionRepository {

  // 1. Use CopyOnWriteArrayList, which is thread-safe for reading and writing.
  private final List<Transaction> transactionStore = new CopyOnWriteArrayList<>();

  // 2. Use AtomicLong to safely generate new, unique IDs.
  private final AtomicLong idGenerator = new AtomicLong(0L);

  @Override
  public Transaction save(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }

    // Transactions are append-only; we don't edit them.
    if (transaction.getId() == null) {
      long newId = idGenerator.incrementAndGet();
      transaction.setId(newId);
      transactionStore.add(transaction);
    }
    return transaction;
  }

  @Override
  public List<Transaction> findAllByAccount(Account account) {
    if (account == null || account.getId() == null) {
      return List.of(); // Return an empty, immutable list
    }

    // Simulate "WHERE account_id = ?" and order by date
    return transactionStore.stream()
        .filter(tx -> account.equals(tx.getAccount()))
        .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // Newest first
        .collect(Collectors.toList());
  }

  @Override
  public List<Transaction> findAllByAccountAndTimestampBetween(Account account, LocalDateTime startDate,
      LocalDateTime endDate) {
    if (account == null || account.getId() == null) {
      return List.of();
    }

    // Simulate the "WHERE account_id = ? AND timestamp BETWEEN ? AND ?" query
    return transactionStore.stream()
        .filter(tx -> account.equals(tx.getAccount()))
        .filter(tx -> !tx.getTimestamp().isBefore(startDate))
        .filter(tx -> !tx.getTimestamp().isAfter(endDate))
        .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // Newest first
        .collect(Collectors.toList());
  }
}
