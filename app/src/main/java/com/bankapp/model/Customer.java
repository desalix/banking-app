package com.bankapp.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity // 1. Tells JPA this class is a database entity
@Table(name = "customers") // 2. Maps this class to the 'customers' table in MySQL
public class Customer {

    @Id // 3. Marks this as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Uses MySQL's auto-increment feature
    private Long id;

    @Column(unique = true, nullable = false) // 5. Adds database constraints
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_joined", nullable = false)
    private LocalDate dateJoined;

    @Embedded // 6. Tells JPA to "embed" the fields from the Address class here
    private Address address;

    // 7. This is the "One-to-Many" relationship mapping
    @OneToMany(
        mappedBy = "customer", // "customer" is the field name in the Account class
        cascade = CascadeType.ALL, // If you delete a Customer, delete their accounts
        orphanRemoval = true, // If you remove an account from this set, delete it
        fetch = FetchType.LAZY // IMPORTANT: Don't load accounts until we ask for them
    )
    private Set<Account> accounts = new HashSet<>();

    // --- Constructors ---

    /**
     * JPA requires a no-argument constructor.
     */
    public Customer() {
    }

    /**
     * A "business" constructor for creating new customers.
     */
    public Customer(String username, String passwordHash, String firstName, String lastName, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateJoined = LocalDate.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    // --- Relationship Helper Methods ---

    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setCustomer(this);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
        account.setCustomer(null);
    }

    // --- equals(), hashCode(), and toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Use a "business key" (like username) for equality, not the 'id'
        Customer customer = (Customer) o;
        return Objects.equals(username, customer.username);
    }

    @Override
    public int hashCode() {
        // Use the same business key for the hash code
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        // IMPORTANT: Do NOT include 'accounts' in toString()
        // It can cause a LazyInitializationException or log spam
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
