package com.fintrack.backend.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

/**
 * Authenticated principal derived from a verified Firebase ID token.
 *
 * <p>Instances of this record are placed in the Spring Security context by
 * {@link FirebaseTokenFilter} after a token is successfully verified. Only
 * claims extracted from the verified token itself are stored here — no
 * database lookup is performed at this stage.
 *
 * @param uid   Firebase UID (the subject claim, always present)
 * @param email Email address from the token (may be null for anonymous users)
 */
public record FirebasePrincipal(String uid, String email) {

    /**
     * Returns an empty authority collection.
     *
     * <p>Role/permission resolution against application data will be added in a
     * later milestone when the user-profile entity exists. For now, the
     * authenticated principal carries only identity claims.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
