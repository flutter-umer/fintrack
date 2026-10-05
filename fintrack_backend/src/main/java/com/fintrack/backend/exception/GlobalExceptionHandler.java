package com.fintrack.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Map;

/**
 * Global exception handler for FinTrack Backend.
 *
 * <p>Returns consistent JSON error envelopes for all unhandled exceptions.
 * As new features are added, add specific {@link ExceptionHandler} methods here
 * rather than leaking stack traces or inconsistent error shapes to the client.
 *
 * <p>Error envelope shape:
 * <pre>
 * {
 *   "timestamp": "2026-10-05T14:00:00Z",
 *   "status": 500,
 *   "error": "Internal Server Error",
 *   "message": "An unexpected error occurred"
 * }
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 404 — unmapped routes. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex) {
        return errorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    /** Catch-all for anything not handled explicitly. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error", "An unexpected error occurred");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> errorResponse(HttpStatus status,
                                                               String error,
                                                               String message) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", error,
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
