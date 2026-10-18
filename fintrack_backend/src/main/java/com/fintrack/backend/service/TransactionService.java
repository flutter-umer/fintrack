package com.fintrack.backend.service;

import com.fintrack.backend.dto.TransactionRequest;
import com.fintrack.backend.dto.TransactionResponse;
import com.fintrack.backend.entity.Transaction;
import com.fintrack.backend.exception.TransactionNotFoundException;
import com.fintrack.backend.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Business logic for income and expense transactions.
 *
 * <p>All methods require a verified Firebase UID. The UID is obtained from the
 * authenticated {@link com.fintrack.backend.security.FirebasePrincipal} in the
 * controller and passed here — it is never read from request bodies.
 *
 * <p>Data ownership is enforced at both the service layer (by passing ownerUid
 * to every query) and the repository layer (via ownership-scoped derived queries).
 */
@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    // ── Create ───────────────────────────────────────────────────────────────

    /**
     * Creates a new transaction for the given Firebase user.
     *
     * @param ownerUid verified Firebase UID from the authenticated principal
     * @param request  validated inbound data — does not contain ownerUid
     * @return the persisted transaction as a response DTO
     */
    @Transactional
    public TransactionResponse create(String ownerUid, TransactionRequest request) {
        Transaction tx = new Transaction();
        tx.setOwnerUid(ownerUid);
        tx.setType(request.type());
        tx.setAmount(request.amount());
        tx.setDescription(request.description());
        tx.setCategory(request.category());
        tx.setTransactionDate(request.transactionDate());
        tx.setCreatedAt(Instant.now());

        return TransactionResponse.from(repository.save(tx));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    /**
     * Returns all transactions for the given Firebase user, newest first.
     */
    public List<TransactionResponse> listForUser(String ownerUid) {
        return repository.findByOwnerUidOrderByTransactionDateDesc(ownerUid)
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }

    /**
     * Returns one transaction if it exists and belongs to the given user.
     *
     * @throws TransactionNotFoundException if not found or wrong owner
     */
    public TransactionResponse getForUser(Long id, String ownerUid) {
        return repository.findByIdAndOwnerUid(id, ownerUid)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    /**
     * Replaces all mutable fields of one transaction for the given user.
     *
     * @throws TransactionNotFoundException if not found or wrong owner
     */
    @Transactional
    public TransactionResponse update(Long id, String ownerUid, TransactionRequest request) {
        Transaction tx = repository.findByIdAndOwnerUid(id, ownerUid)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        tx.setType(request.type());
        tx.setAmount(request.amount());
        tx.setDescription(request.description());
        tx.setCategory(request.category());
        tx.setTransactionDate(request.transactionDate());
        // createdAt and ownerUid are intentionally not updated

        return TransactionResponse.from(repository.save(tx));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    /**
     * Deletes one transaction for the given user.
     *
     * @throws TransactionNotFoundException if not found or wrong owner
     */
    @Transactional
    public void delete(Long id, String ownerUid) {
        long deleted = repository.deleteByIdAndOwnerUid(id, ownerUid);
        if (deleted == 0) {
            throw new TransactionNotFoundException(id);
        }
    }
}
