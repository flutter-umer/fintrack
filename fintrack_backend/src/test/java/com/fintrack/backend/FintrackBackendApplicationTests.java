package com.fintrack.backend;

import com.fintrack.backend.config.SecurityConfig;
import com.fintrack.backend.controller.HealthController;
import com.fintrack.backend.security.FirebaseTokenVerifier;
import com.google.firebase.auth.FirebaseToken;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Slice tests for the health endpoint and Firebase token filter.
 *
 * <p>Uses {@code @WebMvcTest} (loads only the web layer) so no database connection
 * or real Firebase credentials are required. {@link FirebaseTokenVerifier} is
 * provided as a {@code @MockitoBean}, allowing individual tests to stub verification
 * behavior without live Firebase credentials or network access.
 *
 * <p>Spring Boot 4.x note: {@code @WebMvcTest} moved from
 * {@code org.springframework.boot.test.autoconfigure.web.servlet} (Boot 3.x) to
 * {@code org.springframework.boot.webmvc.test.autoconfigure} (Boot 4.x).
 */
@WebMvcTest(HealthController.class)
@Import(SecurityConfig.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked FirebaseTokenVerifier — replaces the real FirebaseConfig bean in the
     * web-layer slice context. Each test stubs it individually as needed.
     * By default (unstubbed), Mockito returns null, which causes the filter to
     * throw, leaving the security context empty and resulting in 401 for protected routes.
     */
    @MockitoBean
    private FirebaseTokenVerifier tokenVerifier;

    // ── Public endpoint ──────────────────────────────────────────────────────

    @Test
    @WithAnonymousUser
    void healthEndpoint_returnsUpWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/health").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("FinTrack Backend"));
    }

    // ── Protected route — no token ───────────────────────────────────────────

    @Test
    @WithAnonymousUser
    void protectedRoute_returns401WhenNoTokenProvided() throws Exception {
        // No Authorization header — filter skips verification, leaves security
        // context empty, and Spring Security's AuthenticationEntryPoint returns 401.
        mockMvc.perform(get("/api/v1/unknown").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ── Protected route — invalid token ─────────────────────────────────────

    @Test
    @WithAnonymousUser
    void protectedRoute_returns401WhenTokenIsInvalid() throws Exception {
        // Stub the verifier to throw — simulates an expired or tampered token.
        when(tokenVerifier.verify(anyString()))
                .thenThrow(new Exception("Token verification failed"));

        mockMvc.perform(get("/api/v1/unknown")
                        .header("Authorization", "Bearer invalid.token.value")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // ── Protected route — valid token ────────────────────────────────────────

    @Test
    void protectedRoute_returns404WhenValidTokenProvided() throws Exception {
        // Stub the verifier to return a successfully verified token.
        FirebaseToken mockToken = mock(FirebaseToken.class);
        when(mockToken.getUid()).thenReturn("test-uid-123");
        when(mockToken.getEmail()).thenReturn("user@example.com");
        when(tokenVerifier.verify(anyString())).thenReturn(mockToken);

        // /api/v1/unknown does not exist — a 404 proves:
        //   (a) the token was accepted (not 401), and
        //   (b) the route is genuinely absent (not a security bypass).
        mockMvc.perform(get("/api/v1/unknown")
                        .header("Authorization", "Bearer valid.token.value")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
