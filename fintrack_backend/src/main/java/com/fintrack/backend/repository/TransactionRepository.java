package com.fintrack.backend.repository;

import com.fintrack.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Data access layer for {@link Transaction} entities.
 *
 * <p>All query methods filter by {@code ownerUid} to enforce data ownership
 * at the database layer. No method returns transactions across multiple owners.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Returns all transactions belonging to the specified owner, most recent first.
     */
    List<Transaction> findByOwnerUidOrderByTransactionDateDesc(String ownerUid);

    /**
     * Returns a transaction by ID only if it belongs to the specified owner.
     *
     * <p>If the ID exists but belongs to a different user, this returns
     * {@code Optional.empty()} — identical to the not-found case — preventing
     * any information leakage about other users' data.
     */
    Optional<Transaction> findByIdAndOwnerUid(Long id, String ownerUid);

    /**
     * Deletes a transaction only if it belongs to the specified owner.
     *
     * @return number of deleted records (0 if not found or wrong owner)
     */
    long deleteByIdAndOwnerUid(Long id, String ownerUid);
}
