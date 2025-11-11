# Banking App Data Model

This document outlines the professional-grade data model for the desktop banking application. The model prioritizes **Separation of Concerns** by dividing the application into:

1.  **Model/Entity Classes:** Plain Old Java Objects (POJOs) that hold data and map to database tables. They are "dumb" and contain no business logic.
2.  **Helper Classes:** Enums and Embeddable classes that strengthen the model.
3.  **Service Layer:** Interfaces that define the business logic ("contracts") for how the application operates.

---

## 🏛️ Core Model/Entity Classes

These are the primary data containers, intended to be mapped by JPA/Hibernate.

### 1. `Customer`

Represents the user of the bank.

* **Attributes:**
    * `private Long id;` (Primary Key)
    * `private String username;` (Unique, for login)
    * `private String passwordHash;` (Hashed and salted password)
    * `private String firstName;`
    * `private String lastName;`
    * `private String email;` (Unique)
    * `private String phoneNumber;`
    * `private Address address;` (**Embeddable**, see below)
    * `private java.time.LocalDate dateJoined;`
    * `private java.util.Set<Account> accounts;` (**One-to-Many:** One customer, many accounts)
* **Methods:**
    * Standard `getters/setters` for all attributes.
    * `addAccount(Account account)` / `removeAccount(Account account)` (Helper methods to manage the bidirectional relationship)

### 2. `Account`

Represents a single bank account (Checking, Savings).

* **Attributes:**
    * `private Long id;` (Primary Key)
    * `private String accountNumber;` (A unique, human-readable string)
    * `private AccountType accountType;` (**Enum**, see below)
    * `private java.math.BigDecimal balance;` (**Critical:** Use `BigDecimal` for currency)
    * `private java.time.LocalDate openedDate;`
    * `private Customer customer;` (**Many-to-One:** Links back to the `Customer`)
* **Methods:**
    * Standard `getters/setters` for all attributes.
    * *(Note: No `List<Transaction>`. This is an efficiency decision. We will query for transactions separately.)*

### 3. `Transaction`

Represents a single atomic entry in a ledger (a single debit or credit).

* **Attributes:**
    * `private Long id;` (Primary Key)
    * `private java.math.BigDecimal amount;` (Positive for deposits, negative for withdrawals)
    * `private TransactionType type;` (**Enum**, see below)
    * `private java.time.LocalDateTime timestamp;` (When the transaction occurred)
    * `private String description;` (e.g., "ATM Withdrawal", "Transfer from J. Doe")
    * `private java.util.UUID correlationId;` (**Pro-tip:** Used to link the two sides of a transfer)
    * `private Account account;` (**Many-to-One:** Links back to the `Account`)
* **Methods:**
    * Standard `getters/setters` for all attributes.

---

## 🧩 Helper Classes (Enums & Embeddables)

### 4. `Address` (Embeddable)

A class to group address fields. This will be embedded directly into the `Customer` table.

* **Attributes:**
    * `private String street;`
    * `private String city;`
    * `private String state;`
    * `private String zipCode;`
    * `private String country;`
* **Methods:**
    * Standard `getters/setters`.

### 5. `AccountType` (Enum)

Restricts account types to a predefined list.

* **Values:**
    * `CHECKING`
    * `SAVINGS`

### 6. `TransactionType` (Enum)

Restricts transaction types. A `TRANSFER` is modeled as one `WITHDRAWAL` and one `DEPOSIT`.

* **Values:**
    * `DEPOSIT`
    * `WITHDRAWAL`

---

## ⚙️ Service Layer (Business Logic Interfaces)

These interfaces define the application's functionality. Your JavaFX controllers will interact with these.

### 7. `CustomerService` (Interface)

* **Methods:**
    * `Customer registerCustomer(String username, String password, String firstName, ...)`
    * `Customer login(String username, String password)`
    * `Customer getCustomerDetails(Long customerId)`
    * `Customer updateCustomerDetails(Long customerId, Customer updatedInfo)`

### 8. `AccountService` (Interface)

* **Methods:**
    * `Account createAccount(Long customerId, AccountType type)`
    * `Account getAccount(String accountNumber)`
    * `java.util.Set<Account> getAccountsForCustomer(Long customerId)`
    * `java.math.BigDecimal getAccountBalance(String accountNumber)`

### 9. `TransactionService` (Interface)

* **Methods:**
    * `Transaction deposit(String toAccountNumber, BigDecimal amount, String description)`
    * `Transaction withdraw(String fromAccountNumber, BigDecimal amount, String description)`
    * `List<Transaction> transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description)`
    * `List<Transaction> getTransactionsForAccount(String accountNumber)`
    * `List<Transaction> getTransactionsForAccount(String accountNumber, LocalDate startDate, LocalDate endDate)`
