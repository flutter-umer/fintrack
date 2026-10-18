package com.fintrack.backend.dto;

import com.fintrack.backend.entity.Transaction;
import com.fintrack.backend.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Outbound DTO for a transaction resource.
 *
 * <p>The {@code ownerUid} is included so clients can verify they received the
 * correct record. It is derived from the entity, never from untrusted input.
 */
public record TransactionResponse(
        Long id,
        String ownerUid,
        TransactionType type,
        BigDecimal amount,
        String description,
        String category,
        LocalDate transactionDate,
        Instant createdAt
) {

    /** Factory method — converts a {@link Transaction} entity to this DTO. */
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getOwnerUid(),
                t.getType(),
                t.getAmount(),
                t.getDescription(),
                t.getCategory(),
                t.getTransactionDate(),
                t.getCreatedAt()
        );
    }
}
