package com.fintrack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FinTrack Backend application entry point.
 *
 * <p>This application uses Firebase Authentication as its identity provider.
 * Spring's default in-memory UserDetailsService is not used. It is excluded
 * via {@code spring.autoconfigure.exclude} in application.properties to
 * suppress the spurious random-password warning at startup.
 *
 * <p>The custom SecurityFilterChain in
 * {@link com.fintrack.backend.config.SecurityConfig} enforces the actual
 * security posture. Firebase token verification will be added in Milestone 2.
 */
@SpringBootApplication
public class FintrackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintrackBackendApplication.class, args);
    }
}
