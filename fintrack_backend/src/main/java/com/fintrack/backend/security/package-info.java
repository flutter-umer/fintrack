/**
 * Security components for FinTrack Backend.
 *
 * <p>Contents (Milestone 2 — Firebase Authentication — implemented):
 * <ul>
 *   <li>{@link com.fintrack.backend.security.FirebaseTokenVerifier} — functional interface
 *       over {@code FirebaseAuth.verifyIdToken()}, extracted for testability.</li>
 *   <li>{@link com.fintrack.backend.security.FirebaseTokenFilter} — validates the
 *       {@code Authorization: Bearer <idToken>} header on every request and populates
 *       the Spring Security context on success.</li>
 *   <li>{@link com.fintrack.backend.security.FirebasePrincipal} — lightweight record
 *       holding the Firebase UID and email extracted from a verified token.</li>
 * </ul>
 *
 * <p>Firebase initialization lives in
 * {@link com.fintrack.backend.config.FirebaseConfig}. Credentials are supplied via
 * the {@code GOOGLE_APPLICATION_CREDENTIALS} environment variable.
 *
 * <p>NOTE: NEVER place Firebase service-account JSON content inside source files.
 * Provide the service account path via the {@code GOOGLE_APPLICATION_CREDENTIALS}
 * environment variable or equivalent secrets mechanism.
 */
package com.fintrack.backend.security;
