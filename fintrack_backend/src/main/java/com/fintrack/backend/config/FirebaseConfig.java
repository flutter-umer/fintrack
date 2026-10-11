package com.fintrack.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.fintrack.backend.security.FirebaseTokenVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Firebase Admin SDK initialization.
 *
 * <p>Credentials are loaded via Application Default Credentials (ADC). For local
 * development, point {@code GOOGLE_APPLICATION_CREDENTIALS} to a service-account
 * JSON file downloaded from the Firebase Console. In CI/production, use a
 * workload-identity mechanism or an injected secret.
 *
 * <p>If no credentials are available, the application still starts and the health
 * endpoint remains accessible. Protected routes will return 401 because the
 * {@link FirebaseTokenVerifier} bean throws on every call when Firebase is not
 * configured — authentication always fails closed.
 *
 * <p>NEVER commit service-account JSON files to source control.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    /**
     * Provides a {@link FirebaseTokenVerifier} bean.
     *
     * <p>Attempts to initialize Firebase using Application Default Credentials.
     * If credentials are absent, returns a verifier that always throws so that
     * no unauthenticated request can ever succeed on a protected route.
     *
     * <p>Reuses an existing default {@link FirebaseApp} if one is already
     * initialized (e.g., in embedded test contexts) to avoid duplicate
     * initialization errors from the Firebase SDK.
     */
    @Bean
    public FirebaseTokenVerifier firebaseTokenVerifier() {
        // If a FirebaseApp was already initialized (e.g., in a test context), reuse it.
        if (!FirebaseApp.getApps().isEmpty()) {
            FirebaseApp app = FirebaseApp.getInstance();
            return idToken -> FirebaseAuth.getInstance(app).verifyIdToken(idToken);
        }

        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase Admin SDK initialized successfully.");
            return idToken -> FirebaseAuth.getInstance(app).verifyIdToken(idToken);

        } catch (IOException ex) {
            log.warn("Firebase credentials not found (GOOGLE_APPLICATION_CREDENTIALS not set or " +
                     "ADC unavailable). The application will start, but all protected API routes " +
                     "will reject requests. Set GOOGLE_APPLICATION_CREDENTIALS to a service-account " +
                     "JSON file to enable Firebase token verification.");

            // Fail closed: every token verification attempt throws so protected routes
            // remain inaccessible when Firebase is not configured.
            return idToken -> {
                throw new IllegalStateException(
                        "Firebase is not configured. Set GOOGLE_APPLICATION_CREDENTIALS.");
            };
        }
    }
}
