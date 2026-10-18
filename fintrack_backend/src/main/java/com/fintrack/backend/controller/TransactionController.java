package com.fintrack.backend.controller;

import com.fintrack.backend.dto.TransactionRequest;
import com.fintrack.backend.dto.TransactionResponse;
import com.fintrack.backend.security.FirebasePrincipal;
import com.fintrack.backend.service.TransactionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for income and expense transactions.
 *
 * <p>All endpoints require a valid Firebase ID token (enforced by
 * {@link com.fintrack.backend.security.FirebaseTokenFilter}). The authenticated
 * {@link FirebasePrincipal} is injected via {@code @AuthenticationPrincipal} and
 * its UID is passed to the service layer. Client-supplied owner IDs are never accepted.
 *
 * <p>Base path: {@code /api/v1/transactions}
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * Creates a new transaction for the authenticated user.
     *
     * <p>POST /api/v1/transactions
     * <pre>
     * Request body:
     * {
     *   "type": "EXPENSE",
     *   "amount": "45.00",
     *   "description": "Grocery shopping",
     *   "category": "Food",
     *   "transactionDate": "2026-10-18"
     * }
     * Response: 201 Created — TransactionResponse body
     * </pre>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(
            @AuthenticationPrincipal FirebasePrincipal principal,
            @Valid @RequestBody TransactionRequest request) {
        return service.create(principal.uid(), request);
    }

    /**
     * Lists all transactions for the authenticated user, newest first.
     *
     * <p>GET /api/v1/transactions
     * <p>Response: 200 OK — array of TransactionResponse
     */
    @GetMapping
    public List<TransactionResponse> list(
            @AuthenticationPrincipal FirebasePrincipal principal) {
        return service.listForUser(principal.uid());
    }

    /**
     * Retrieves one transaction by ID for the authenticated user.
     *
     * <p>GET /api/v1/transactions/{id}
     * <p>Response: 200 OK — TransactionResponse, or 404 if not found / wrong owner
     */
    @GetMapping("/{id}")
    public TransactionResponse get(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebasePrincipal principal) {
        return service.getForUser(id, principal.uid());
    }

    /**
     * Replaces all mutable fields of one transaction for the authenticated user.
     *
     * <p>PUT /api/v1/transactions/{id}
     * <p>Response: 200 OK — updated TransactionResponse, or 404 if not found / wrong owner
     */
    @PutMapping("/{id}")
    public TransactionResponse update(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebasePrincipal principal,
            @Valid @RequestBody TransactionRequest request) {
        return service.update(id, principal.uid(), request);
    }

    /**
     * Deletes one transaction for the authenticated user.
     *
     * <p>DELETE /api/v1/transactions/{id}
     * <p>Response: 204 No Content, or 404 if not found / wrong owner
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal FirebasePrincipal principal) {
        service.delete(id, principal.uid());
        return ResponseEntity.noContent().build();
    }
}
