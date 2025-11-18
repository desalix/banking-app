package com.bankapp.service.impl;

import com.bankapp.model.Account;
import com.bankapp.model.AccountType;
import com.bankapp.model.Customer;
import com.bankapp.model.Transaction;
import com.bankapp.model.TransactionType;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.service.BankingService;
import com.bankapp.service.exception.AccountNotFoundException;
import com.bankapp.service.exception.InsufficientFundsException;
import com.bankapp.service.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Set;

public class BankingServiceImpl implements BankingService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  /**
   * Dependency Injection. This service needs both repositories to do its job.
   *
   * @param accountRepository     The repository for managing accounts.
   * @param transactionRepository The repository for managing transactions.
   */
  public BankingServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Transaction deposit(String toAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InvalidAmountException {

    // 1. --- Validation ---
    validateAmount(amount);
    Account account = getAccount(toAccountNumber); // Re-uses our own method

    // 2. --- Business Logic ---
    account.setBalance(account.getBalance().add(amount));

    // 3. --- Create Ledger Entry ---
    Transaction tx = new Transaction(
        account,
        TransactionType.DEPOSIT,
        amount,
        "Deposit");

    // 4. --- Persist Changes ---
    accountRepository.save(account);
    return transactionRepository.save(tx);
  }

  @Override
  public Transaction withdraw(String fromAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {

    // 1. --- Validation ---
    validateAmount(amount);
    Account account = getAccount(fromAccountNumber);
    validateFunds(account, amount); // Check for sufficient funds

    // 2. --- Business Logic ---
    account.setBalance(account.getBalance().subtract(amount));

    // 3. --- Create Ledger Entry ---
    Transaction tx = new Transaction(
        account,
        TransactionType.WITHDRAWAL,
        amount.negate(), // Store withdrawals as a negative number
        "Withdrawal");

    // 4. --- Persist Changes ---
    accountRepository.save(account);
    return transactionRepository.save(tx);
  }

  @Override
  public List<Transaction> transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount)
      throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {

    // 1. --- Validation ---
    validateAmount(amount);
    Account fromAccount = getAccount(fromAccountNumber);
    Account toAccount = getAccount(toAccountNumber);
    validateFunds(fromAccount, amount);

    // 2. --- Business Logic (The Transfer) ---
    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
    toAccount.setBalance(toAccount.getBalance().add(amount));

    // 3. --- Create Ledger Entries ---
    // Use a Correlation ID to link these two transactions
    UUID correlationId = UUID.randomUUID();
    String descTo = "Transfer from " + fromAccount.getCustomer().getFirstName();
    String descFrom = "Transfer to " + toAccount.getCustomer().getFirstName();

    Transaction withdrawalTx = new Transaction(
        fromAccount,
        TransactionType.WITHDRAWAL,
        amount.negate(), // Negative
        descFrom);
    withdrawalTx.setCorrelationId(correlationId);

    Transaction depositTx = new Transaction(
        toAccount,
        TransactionType.DEPOSIT,
        amount, // Positive
        descTo);
    depositTx.setCorrelationId(correlationId);

    // 4. --- Persist Changes ---
    // In a real app, this would be one "atomic" database transaction
    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);
    Transaction savedWithdrawal = transactionRepository.save(withdrawalTx);
    Transaction savedDeposit = transactionRepository.save(depositTx);

    return List.of(savedWithdrawal, savedDeposit);
  }

  @Override
  public List<Transaction> getTransactionsForAccount(String accountNumber) throws AccountNotFoundException {
    Account account = getAccount(accountNumber);
    return transactionRepository.findAllByAccount(account);
  }

  @Override
  public Account getAccount(String accountNumber) throws AccountNotFoundException {
    return accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFoundException("No account found with number: " + accountNumber));
  }

  // --- Private Helper Methods ---

  /**
   * A private helper to validate a transaction amount.
   */
  private void validateAmount(BigDecimal amount) throws InvalidAmountException {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException("Amount must be greater than zero.");
    }
  }

  /**
   * A private helper to check if an account has enough funds.
   */
  private void validateFunds(Account account, BigDecimal amount) throws InsufficientFundsException {
    if (account.getBalance().compareTo(amount) < 0) {
      throw new InsufficientFundsException("Insufficient funds for this transaction.");
    }
  }

  @Override
  public Account createAccount(Customer customer, AccountType type, String accountNumber) {
    Account newAccount = new Account(customer, type, accountNumber);
    return accountRepository.save(newAccount);
  }

  @Override
  public Set<Account> getAccountsForCustomer(Long customerId) {
    // We create a dummy customer object just to pass the ID to the repository
    Customer customer = new Customer();
    customer.setId(customerId);
    return accountRepository.findAllByCustomer(customer);
  }
}
