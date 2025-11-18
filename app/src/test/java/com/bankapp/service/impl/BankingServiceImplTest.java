package com.bankapp.service.impl;

import com.bankapp.model.Account;
import com.bankapp.model.AccountType;
import com.bankapp.model.Customer;
import com.bankapp.model.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository_impl.mock.MockAccountRepository;
import com.bankapp.repository_impl.mock.MockTransactionRepository;
import com.bankapp.service.BankingService;
import com.bankapp.service.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankingServiceImplTest {

  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;
  private BankingService bankingService;

  // We'll pre-load these accounts for our tests
  private Account fromAccount;
  private Account toAccount;

  @BeforeEach
  void setUp() {
    // 1. Create fresh mock repositories
    accountRepository = new MockAccountRepository();
    transactionRepository = new MockTransactionRepository();

    // 2. Create the service, injecting both mocks
    bankingService = new BankingServiceImpl(accountRepository, transactionRepository);

    // 3. --- ARRANGE ---
    // Create dummy customers for the accounts
    Customer customerA = new Customer("userA", "hash", "A", "A", "a@a.com");
    Customer customerB = new Customer("userB", "hash", "B", "B", "b@b.com");

    // 4. Create two accounts
    fromAccount = new Account(customerA, AccountType.CHECKING, "12345");
    fromAccount.setBalance(new BigDecimal("100.00")); // Give it $100

    toAccount = new Account(customerB, AccountType.CHECKING, "67890");
    toAccount.setBalance(new BigDecimal("50.00")); // Give it $50

    // 5. IMPORTANT: Save the accounts to the mock repo
    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);
  }

  @Test
  void testTransferSuccessfully() throws Exception {
    // --- Arrange ---
    BigDecimal transferAmount = new BigDecimal("75.00");

    // --- Act ---
    List<Transaction> transactions = bankingService.transfer("12345", "67890", transferAmount);

    // --- Assert ---
    // 1. Check transactions
    assertNotNull(transactions);
    assertEquals(2, transactions.size());

    // 2. Check final balances
    Account finalFrom = accountRepository.findByAccountNumber("12345").get();
    Account finalTo = accountRepository.findByAccountNumber("67890").get();

    // Use compareTo for BigDecimal equality
    assertEquals(0, finalFrom.getBalance().compareTo(new BigDecimal("25.00"))); // 100 - 75
    assertEquals(0, finalTo.getBalance().compareTo(new BigDecimal("125.00"))); // 50 + 75

    // 3. Check that transactions were saved
    assertEquals(1, transactionRepository.findAllByAccount(fromAccount).size());
    assertEquals(1, transactionRepository.findAllByAccount(toAccount).size());
  }

  @Test
  void testTransferWithInsufficientFunds() {
    // --- Arrange ---
    // Try to transfer $100.01 from an account with $100.00
    BigDecimal transferAmount = new BigDecimal("100.01");

    // --- Act & Assert ---
    // 1. Verify the correct exception is thrown
    InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
      bankingService.transfer("12345", "67890", transferAmount);
    });

    // 2. (Optional) Check error message
    assertTrue(exception.getMessage().contains("Insufficient funds"));

    // 3. CRITICAL: Verify that NO money was moved
    Account finalFrom = accountRepository.findByAccountNumber("12345").get();
    Account finalTo = accountRepository.findByAccountNumber("67890").get();

    // Balances should be unchanged
    assertEquals(0, finalFrom.getBalance().compareTo(new BigDecimal("100.00")));
    assertEquals(0, finalTo.getBalance().compareTo(new BigDecimal("50.00")));

    // 4. Verify NO transactions were created
    assertEquals(0, transactionRepository.findAllByAccount(fromAccount).size());
    assertEquals(0, transactionRepository.findAllByAccount(toAccount).size());
  }
}
