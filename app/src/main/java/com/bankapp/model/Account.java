package com.bankapp.model;

import java.time.LocalDate;
import java.math.BigDecimal;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity // Tells JPA this class is a database entity
@Table(name = "accounts") // Maps this class to the 'accounts' table in MySQL
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountType accountType;

  @Column(nullable = false)
  private BigDecimal balance; // Always BigDecimal for money

  @Column(name = "opened_date", nullable = false)
  private LocalDate openedDate;

  @ManyToOne(fetch = FetchType.LAZY) // LAZY = not loaded until getCustomer() is called
  @JoinColumn(name = "customer_id", nullable = false) // Foreign key behavior with 'accounts' table
  private Customer customer;

  public Account() {

  }

  public Account(Customer customer, AccountType accountType, String accountNumber) {
    this.customer = customer;
    this.accountType = accountType;
    this.accountNumber = accountNumber;
    this.balance = BigDecimal.ZERO; // New accounts have zero for balance
    this.openedDate = LocalDate.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public LocalDate getOpenedDate() {
    return openedDate;
  }

  public void setOpenedDate(LocalDate openedDate) {
    this.openedDate = openedDate;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Account account = (Account) o;
    return Objects.equals(accountNumber, account.accountNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNumber);
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", accountNumber=" + accountNumber +
        ", accountType=" + accountType +
        ", balance=" + balance +
        '}';
  }

}
