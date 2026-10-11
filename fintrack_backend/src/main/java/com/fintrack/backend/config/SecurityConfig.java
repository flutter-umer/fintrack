package com.fintrack.backend.config;

import com.fintrack.backend.security.FirebaseTokenFilter;
import com.fintrack.backend.security.FirebaseTokenVerifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for FinTrack Backend.
 *
 * <p><strong>Current state (Milestone 2 — Firebase Authentication):</strong>
 * The {@link FirebaseTokenFilter} validates {@code Authorization: Bearer <idToken>}
 * headers on every request. The health endpoint remains publicly accessible.
 * All other endpoints require a valid Firebase ID token.
 *
 * <p>On a missing or invalid token, protected routes return {@code 401 Unauthorized}.
 * The filter itself does not reject requests — it populates the security context on
 * success and leaves it empty on failure, relying on Spring Security's authorization
 * layer to enforce the deny-all default.
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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            FirebaseTokenVerifier tokenVerifier) throws Exception {

        http
            // Stateless REST API — no HTTP sessions needed.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // CSRF protection is irrelevant for a token-based stateless API.
            .csrf(AbstractHttpConfigurer::disable)

            // Return 401 for unauthenticated requests to protected routes
            // instead of the default redirect behavior.
            .exceptionHandling(ex ->
                ex.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

            // Authorization rules.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                // All other requests require a verified Firebase ID token.
                .anyRequest().authenticated()
            )

            // Disable Spring's default form login and HTTP Basic.
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // Insert Firebase token verification before Spring's username/password filter.
            .addFilterBefore(
                    new FirebaseTokenFilter(tokenVerifier),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
