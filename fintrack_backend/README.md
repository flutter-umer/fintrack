# FinTrack Backend

Spring Boot REST API for the FinTrack personal finance management application.

## Overview

This directory contains the backend service for FinTrack. It is a stateless REST API that will store and serve financial data to the Flutter mobile application. Authentication is delegated to Firebase Authentication — the backend is designed to verify Firebase ID tokens on protected routes rather than managing credentials itself.

The backend is currently at the foundation milestone: the project structure, security configuration, and a health endpoint are in place. No business features have been implemented yet.

## Technology Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Web (MVC) | managed by Spring Boot |
| Spring Data JPA | managed by Spring Boot |
| Hibernate | managed by Spring Boot |
| Spring Security | managed by Spring Boot |
| Spring Validation | managed by Spring Boot |
| PostgreSQL driver | managed by Spring Boot |
| Maven | 3.9.x |

Spring Boot 4.x uses Jackson 3 (`tools.jackson`) for JSON serialization.

## Package Structure

```
src/main/java/com/fintrack/backend/
├── FintrackBackendApplication.java   # Application entry point
├── config/                           # Spring configuration classes
│   └── SecurityConfig.java           # HTTP security filter chain
├── controller/                       # REST controllers
│   └── HealthController.java         # Health check endpoint
├── dto/                              # Request and response data transfer objects
│   └── HealthResponse.java           # Health check response record
├── exception/                        # Exception handling
│   └── GlobalExceptionHandler.java   # Global @RestControllerAdvice
├── security/                         # Security filters and principals (planned)
├── entity/                           # JPA entity classes (planned)
├── repository/                       # Spring Data JPA repositories (planned)
├── service/                          # Business logic services (planned)
└── mapper/                           # Object mappers (planned)
```

## Implemented Features

### Health Check

Returns the application liveness status. Does not require authentication.

```
GET /api/v1/health
```

Response:

```json
{
  "status": "UP",
  "application": "FinTrack Backend"
}
```

### Security Configuration

- Session policy: stateless (no HTTP sessions)
- CSRF: disabled
- Form login and HTTP Basic: disabled
- The health endpoint is publicly accessible
- All other routes return 403 until Firebase token verification is implemented

### Error Handling

All non-2xx responses return a consistent JSON envelope:

```json
{
  "timestamp": "2026-10-05T14:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "..."
}
```

## Database

The backend is configured to connect to a PostgreSQL database. No schema exists yet — entity classes and database migrations will be introduced in a future milestone.

The JPA `ddl-auto` setting is currently `none`. Hibernate will not attempt to create or modify the schema. This will be replaced with Flyway or Liquibase when the first entities are added.

## Configuration

The application reads database credentials from environment variables at startup. Do not place real credentials in `application.properties`.

Copy `src/main/resources/application.properties.example` to `application.properties` and fill in your local values. The `application.properties` file is git-ignored and must not be committed.

| Environment Variable | Default (placeholder) | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/fintrack` | JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | `fintrack_user` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | _(set locally)_ | Database password |

The application listens on port `8080` by default.

## Prerequisites

| Requirement | Details |
|---|---|
| Java | 21 — on this machine: `C:\SDKs\jdk-21.0.12` |
| Maven | 3.9.x |
| PostgreSQL | 15 or later (required to start the full application) |

The system `java` on the development machine defaults to Java 8. Set `JAVA_HOME` explicitly before running Maven commands:

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
```

## Running the Application

### With PostgreSQL

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:SPRING_DATASOURCE_URL      = "jdbc:postgresql://localhost:5432/fintrack"
$env:SPRING_DATASOURCE_USERNAME = "your_db_user"
$env:SPRING_DATASOURCE_PASSWORD = "your_db_password"

mvn spring-boot:run
```

### Without PostgreSQL (health endpoint only)

To verify the health endpoint without a database connection, exclude the JPA and DataSource auto-configurations:

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:SPRING_AUTOCONFIGURE_EXCLUDE = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"

mvn spring-boot:run
```

Then: `GET http://localhost:8080/api/v1/health`

### Building a JAR

```powershell
mvn package -DskipTests
java -jar target/fintrack-backend-0.0.1-SNAPSHOT.jar
```

## Testing

Tests use `@WebMvcTest` (web layer slice) and do not require a running database.

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:MAVEN_OPTS = "-Xmx512m -Xms256m"

mvn test
```

Expected result: `BUILD SUCCESS — Tests run: 1, Failures: 0, Errors: 0`

**Note on Spring Boot 4.x testing:** `@WebMvcTest` moved to the `org.springframework.boot.webmvc.test.autoconfigure` package and requires `spring-boot-starter-webmvc-test` on the test classpath. Security test annotations require `spring-boot-starter-security-test`. Both are declared in `pom.xml`.

## Development Status

**Implemented:**
- Project structure and Maven build
- Spring Security configuration (stateless, deny-all by default)
- `GET /api/v1/health` endpoint
- Global exception handler with consistent error envelope
- Package scaffolding for all planned layers

**Not yet implemented:**
- Firebase token verification
- User profile management
- Transaction CRUD
- Budget management
- Savings goals
- Analytics endpoints
- Database schema and migrations
- CORS configuration

Refer to `PROJECT_STATUS.md` in this directory for the full milestone plan and detailed status.
