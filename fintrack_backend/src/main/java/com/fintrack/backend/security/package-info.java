/**
 * Security components for FinTrack Backend.
 *
 * <p>Planned contents (Milestone 2 — Firebase Authentication):
 * <ul>
 *   <li>{@code FirebaseTokenFilter} — validates the {@code Authorization: Bearer <idToken>}
 *       header on every protected request by calling Firebase Admin SDK's
 *       {@code FirebaseAuth.verifyIdToken()}.</li>
 *   <li>{@code FirebasePrincipal} — lightweight principal holding the Firebase UID,
 *       email, and any custom claims extracted from the verified token.</li>
 * </ul>
 *
 * <p>Firebase Admin SDK is NOT a dependency yet. Add it in Milestone 2:
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;com.google.firebase&lt;/groupId&gt;
 *   &lt;artifactId&gt;firebase-admin&lt;/artifactId&gt;
 *   &lt;version&gt;9.x&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * <p>⚠️ NEVER place Firebase service-account JSON content inside source files.
 * Provide the service account path via the {@code GOOGLE_APPLICATION_CREDENTIALS}
 * environment variable or equivalent secrets mechanism.
 */
package com.fintrack.backend.security;
