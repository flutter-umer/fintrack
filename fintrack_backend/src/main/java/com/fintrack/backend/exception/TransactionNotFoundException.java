package com.fintrack.backend.exception;

/**
 * Thrown when a transaction is not found or does not belong to the requesting user.
 *
 * <p>Using a single exception for both "not found" and "wrong owner" cases
 * intentionally avoids leaking information about other users' transaction IDs.
 * The client receives a 404 in both cases.
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long id) {
        super("Transaction not found: " + id);
    }
}
