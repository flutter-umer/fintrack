package com.fintrack.backend;

import com.fintrack.backend.controller.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fintrack.backend.config.SecurityConfig;

/**
 * Slice test for the health endpoint.
 *
 * <p>Uses {@code @WebMvcTest} (loads only the web layer) so no database connection
 * is required. Security config is imported explicitly to verify that the health
 * endpoint is reachable without authentication, and that all other routes are
 * protected by the deny-all default security posture.
 *
 * <p>Spring Boot 4.x note: {@code @WebMvcTest} moved from
 * {@code org.springframework.boot.test.autoconfigure.web.servlet} (Boot 3.x) to
 * {@code org.springframework.boot.webmvc.test.autoconfigure} (Boot 4.x).
 * The {@code spring-boot-starter-webmvc-test} dependency must be on the test classpath.
 */
@WebMvcTest(HealthController.class)
@Import(SecurityConfig.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void healthEndpoint_returnsUpWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/health").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("FinTrack Backend"));
    }

    @Test
    @WithAnonymousUser
    void unknownRoute_returns403ForUnauthenticatedRequest() throws Exception {
        // Verifies the deny-all default posture in SecurityConfig:
        // any route that is not explicitly listed in PUBLIC_ENDPOINTS must
        // return 403 to an unauthenticated caller. This guards against an
        // accidental blanket permitAll() being introduced in the future.
        mockMvc.perform(get("/api/v1/unknown").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
