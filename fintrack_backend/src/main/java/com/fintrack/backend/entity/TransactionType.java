package com.fintrack.backend.entity;

/**
 * Classification of a financial transaction.
 *
 * <p>Stored as a VARCHAR in PostgreSQL using
 * {@link jakarta.persistence.EnumType#STRING} to preserve readability in the
 * database and survive enum reordering.
 */
public enum TransactionType {
    INCOME,
    EXPENSE
}
