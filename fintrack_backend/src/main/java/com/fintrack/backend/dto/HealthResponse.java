package com.fintrack.backend.dto;

/**
 * Response DTO for the health check endpoint.
 *
 * <p>Intentionally a plain record — no JPA, no validation annotations needed here.
 */
public record HealthResponse(String status, String application) {}
