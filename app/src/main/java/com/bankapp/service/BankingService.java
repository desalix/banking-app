package com.bankapp.service;

import com.bankapp.model.Account;
import com.bankapp.model.AccountType;
import com.bankapp.model.Customer;
import com.bankapp.model.Transaction;
import com.bankapp.service.exception.AccountNotFoundException;
import com.bankapp.service.exception.InsufficientFundsException;
import com.bankapp.service.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Interface (contract) for the Banking Service.
 * This defines the business logic for all core banking operations.
 */
public interface BankingService {

  /**
   * Deposits a positive amount into an account.
   *
   * @param toAccountNumber The account number to deposit into.
   * @param amount          The amount to deposit.
   * @return The resulting Transaction.
   * @throws AccountNotFoundException if the account does not exist.
   * @throws InvalidAmountException   if the amount is zero or negative.
   */
  Transaction deposit(String toAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InvalidAmountException;

  /**
   * Withdraws a positive amount from an account.
   *
   * @param fromAccountNumber The account number to withdraw from.
   * @param amount            The amount to withdraw.
   * @return The resulting Transaction.
   * @throws AccountNotFoundException   if the account does not exist.
   * @throws InsufficientFundsException if the account balance is less than the
   *                                    amount.
   * @throws InvalidAmountException     if the amount is zero or negative.
   */
  Transaction withdraw(String fromAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException;

  /**
   * Transfers a positive amount from one account to another.
   *
   * @param fromAccountNumber The account to transfer from.
   * @param toAccountNumber   The account to transfer to.
   * @param amount            The amount to transfer.
   * @return A list containing the two resulting Transactions (the withdrawal and
   *         the deposit).
   * @throws AccountNotFoundException   if either account does not exist.
   * @throws InsufficientFundsException if the 'from' account has insufficient
   *                                    funds.
   * @throws InvalidAmountException     if the amount is zero or negative.
   */
  List<Transaction> transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException;

  /**
   * Retrieves the list of transactions for a specific account.
   *
   * @param accountNumber The account to get history for.
   * @return A List of Transactions, newest first.
   * @throws AccountNotFoundException if the account does not exist.
   */
  List<Transaction> getTransactionsForAccount(String accountNumber)
      throws AccountNotFoundException;

  /**
   * Retrieves a single Account by its account number.
   *
   * @param accountNumber The account number.
   * @return The Account object.
   * @throws AccountNotFoundException if the account does not exist.
   */
  Account getAccount(String accountNumber)
      throws AccountNotFoundException;

  Account createAccount(Customer customer, AccountType type, String accountNumber);

  /**
   * Retrieves all accounts belonging to a specific customer ID.
   */
  Set<Account> getAccountsForCustomer(Long customerId);
}
