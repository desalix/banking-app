package com.bankapp.util;

import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.CustomerRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository_impl.mock.MockAccountRepository;
import com.bankapp.repository_impl.mock.MockCustomerRepository;
import com.bankapp.repository_impl.mock.MockTransactionRepository;
import com.bankapp.service.AuthService;
import com.bankapp.service.BankingService;
import com.bankapp.service.impl.AuthServiceImpl;
import com.bankapp.service.impl.BankingServiceImpl;

/**
 * A simple Dependency Injection container.
 * It ensures that we use the SAME instance of repositories and services
 * across the entire application, preserving our data in memory.
 */
public class DependencyFactory {

  // 1. Create SINGLE instances of the Repositories
  private static final CustomerRepository customerRepository = new MockCustomerRepository();
  private static final AccountRepository accountRepository = new MockAccountRepository();
  private static final TransactionRepository transactionRepository = new MockTransactionRepository();

  // 2. Create SINGLE instances of the Services, injecting the repositories
  private static final AuthService authService = new AuthServiceImpl(customerRepository);

  private static final BankingService bankingService = new BankingServiceImpl(
      accountRepository,
      transactionRepository);

  // 3. Public methods to access these singletons
  public static AuthService getAuthService() {
    return authService;
  }

  public static BankingService getBankingService() {
    return bankingService;
  }

  // We might need direct repo access later, but for now services are enough.
}
