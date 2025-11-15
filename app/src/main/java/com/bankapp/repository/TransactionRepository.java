package com.bankapp.repository;

import com.bankapp.model.Account;
import com.bankapp.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface (contract) for data access operations related to Transactions.
 */
public interface TransactionRepository {

  /**
   * Saves a new Transaction to the database.
   *
   * @param transaction The transaction object to save.
   * @return The saved transaction with its new ID.
   */
  Transaction save(Transaction transaction);

  /**
   * Finds all transactions for a specific account.
   *
   * @param account The account to get history for.
   * @return A List of transactions, typically ordered by date.
   */
  List<Transaction> findAllByAccount(Account account);

  /**
   * Finds all transactions for a specific account within a date range.
   *
   * @param account   The account to get history for.
   * @param startDate The start of the time window (inclusive).
   * @param endDate   The end of the time window (inclusive).
   * @return A List of transactions from that account within the specified range.
   */
  List<Transaction> findAllByAccountAndTimestampBetween(Account account, LocalDateTime startDate,
      LocalDateTime endDate);

}
