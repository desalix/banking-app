package com.bankapp.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;
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

@Entity
@Table(name = "transactions")
public class Transaction{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransationType type;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private LocalDate timestamp;

  @Column(name = "correlation_id")
  private UUID correlationId; // Links two transactions to a transfer

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;


  public Transaction(){

  }

  public Transaction(Account account, TransactionType type, BigDecimal amount, String description) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now(); // Set timestamp on creation
    }


  public Long getId() {
      return id;
  }

  public void setId(Long id) {
      this.id = id;
  }

  public BigDecimal getAmount() {
      return amount;
  }

  public void setAmount(BigDecimal amount) {
      this.amount = amount;
  }

  public TransactionType getType() {
      return type;
  }

  public void setType(TransactionType type) {
      this.type = type;
  }

  public LocalDateTime getTimestamp() {
      return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
  }

  public String getDescription() {
      return description;
  }

  public void setDescription(String description) {
      this.description = description;
  }

  public UUID getCorrelationId() {
      return correlationId;
  }

  public void setCorrelationId(UUID correlationId) {
      this.correlationId = correlationId;
  }

  public Account getAccount() {
      return account;
  }

  public void setAccount(Account account) {
      this.account = account;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    Transaction that = (Transaction) o;
    return id != null && Objects.equals(id, that.id);
    }

  @Override
  public int hashCode() {
      return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "id=" + id +
              ", amount=" + amount +
              ", type=" + type +
              ", timestamp=" + timestamp +
              ", description='" + description +
            '}';
  }
}
