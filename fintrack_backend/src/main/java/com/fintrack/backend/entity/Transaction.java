package com.fintrack.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Persistent financial transaction owned by a Firebase-authenticated user.
 *
 * <p>The {@code ownerUid} column is populated exclusively from the verified
 * Firebase principal — never from client request bodies. This ensures that
 * every transaction is correctly and securely attributed to its owner.
 *
 * <p>Schema setup: {@code ddl-auto} is {@code none}. Run
 * {@code src/main/resources/db/schema.sql} against your PostgreSQL database
 * before starting the application for the first time.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Firebase UID of the user who owns this transaction.
     * Set by the service layer from the authenticated principal — never from
     * the request body.
     */
    @Column(name = "owner_uid", nullable = false, length = 128)
    private String ownerUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TransactionType type;

    /**
     * Monetary amount — always positive. The {@code type} field records the
     * direction (income vs. expense). Uses {@code NUMERIC(19,4)} in PostgreSQL
     * to avoid floating-point rounding errors.
     */
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // ── Constructors ─────────────────────────────────────────────────────────

    public Transaction() {
        // Required by JPA. Public access allows construction from other packages
        // (e.g., TransactionService) without weakening JPA compatibility.
    }

    // ── Getters and setters ──────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getOwnerUid() { return ownerUid; }
    public void setOwnerUid(String ownerUid) { this.ownerUid = ownerUid; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
