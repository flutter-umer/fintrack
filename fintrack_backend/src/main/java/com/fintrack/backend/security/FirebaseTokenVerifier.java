package com.fintrack.backend.security;

import com.google.firebase.auth.FirebaseToken;

/**
 * Abstraction over the Firebase ID token verification step.
 *
 * <p>This interface exists so that the real {@link com.google.firebase.auth.FirebaseAuth}
 * call can be replaced with a test double in unit and slice tests, removing the
 * need for real Firebase credentials or network access in the test suite.
 *
 * <p>The single production implementation ({@link FirebaseAuthTokenVerifier}) delegates
 * to {@code FirebaseAuth.getInstance().verifyIdToken(token, true)} with clock-skew
 * checking enabled.
 */
@FunctionalInterface
public interface FirebaseTokenVerifier {

    /**
     * Verifies an ID token string and returns the decoded token.
     *
     * @param idToken the raw Firebase ID token from the Authorization header
     * @return the verified, decoded token containing UID and claims
     * @throws Exception if the token is invalid, expired, or cannot be verified
     */
    FirebaseToken verify(String idToken) throws Exception;
}
