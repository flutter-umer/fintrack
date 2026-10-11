package com.fintrack.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseToken;

/**
 * Servlet filter that validates Firebase ID tokens on every incoming request.
 *
 * <p>Extracts the {@code Authorization: Bearer <idToken>} header, delegates
 * verification to {@link FirebaseTokenVerifier}, and — on success — places a
 * {@link UsernamePasswordAuthenticationToken} backed by a {@link FirebasePrincipal}
 * in the {@link SecurityContextHolder}.
 *
 * <p>Requests with a missing or invalid token are not rejected here; they simply
 * proceed without an authenticated principal. Spring Security's authorization
 * rules (configured in {@link com.fintrack.backend.config.SecurityConfig}) then
 * reject un-authenticated access to protected routes with 403.
 *
 * <p>Raw token strings are never logged.
 */
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(FirebaseTokenFilter.class);

    private static final String BEARER_PREFIX = "Bearer ";

    private final FirebaseTokenVerifier tokenVerifier;

    public FirebaseTokenFilter(FirebaseTokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String idToken = authHeader.substring(BEARER_PREFIX.length());

            try {
                FirebaseToken verified = tokenVerifier.verify(idToken);

                FirebasePrincipal principal = new FirebasePrincipal(
                        verified.getUid(),
                        verified.getEmail()
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,                         // credentials — not stored
                                principal.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                // Token is invalid or unverifiable. Clear any stale context and
                // let the request proceed unauthenticated. SecurityConfig will
                // reject it for protected routes.
                SecurityContextHolder.clearContext();
                log.debug("Firebase token verification failed: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
