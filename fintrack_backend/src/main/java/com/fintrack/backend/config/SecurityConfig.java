package com.fintrack.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for FinTrack Backend.
 *
 * <p><strong>Current state (Milestone 1 — Foundation):</strong>
 * The health endpoint is publicly accessible. All other endpoints require authentication.
 *
 * <p><strong>Future state (Milestone 2 — Firebase Auth):</strong>
 * A {@code FirebaseTokenFilter} will be inserted before
 * {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter}.
 * It will validate the Firebase ID token from the {@code Authorization: Bearer <token>} header,
 * extract the Firebase UID, and set a {@link org.springframework.security.core.Authentication}
 * in the {@link org.springframework.security.core.context.SecurityContext}. No Spring-managed
 * users or sessions will be needed — Firebase is the identity provider.
 *
 * <p>CSRF is disabled because the API is stateless (no session, no cookie auth).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Public endpoints that do not require a Firebase ID token.
     * Extend this list cautiously — the default posture is deny-all.
     */
    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/v1/health"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API — no HTTP sessions needed.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CSRF protection is irrelevant for a token-based stateless API.
            .csrf(AbstractHttpConfigurer::disable)

            // Authorization rules.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                // All other requests require authentication.
                // TODO (Milestone 2): replace with Firebase token filter + authenticated() check.
                .anyRequest().authenticated()
            )

            // Disable Spring's default form login and HTTP Basic — this API uses Firebase tokens.
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
