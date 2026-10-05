package com.fintrack.backend.controller;

import com.fintrack.backend.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check controller.
 *
 * <p>Provides a simple liveness signal for load balancers, monitoring systems,
 * and integration tests. Does not require authentication.
 *
 * <p>GET /api/v1/health → 200 OK
 * <pre>
 * {
 *   "status": "UP",
 *   "application": "FinTrack Backend"
 * }
 * </pre>
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "FinTrack Backend"));
    }
}
