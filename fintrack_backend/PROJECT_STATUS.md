# FinTrack Backend — PROJECT_STATUS.md
## Spring Boot REST API — Development Status & AI Handoff Document

> **Document type:** Single source of truth for the backend application.
> **Audience:** Developer, future contributors, AI coding assistants.
> **Last updated:** 2026-10-05
> **⚠️ Never copy credentials, API keys, service-account files, or secrets into this file.**

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Project Structure](#3-project-structure)
4. [Dependencies](#4-dependencies)
5. [How to Run](#5-how-to-run)
6. [Health Endpoint](#6-health-endpoint)
7. [Architecture Overview](#7-architecture-overview)
8. [Security Design](#8-security-design)
9. [Current Status](#9-current-status)
10. [Next Milestones](#10-next-milestones)
11. [Assumptions & Decisions Made](#11-assumptions--decisions-made)
12. [Known Issues & Constraints](#12-known-issues--constraints)
13. [AI Handoff Notes](#13-ai-handoff-notes)
14. [Changelog](#14-changelog)

---

## 1. Project Overview

| Field | Value |
|---|---|
| **Project name** | FinTrack Backend |
| **Artifact ID** | `fintrack-backend` |
| **Group ID** | `com.fintrack` |
| **Version** | `0.0.1-SNAPSHOT` |
| **Project type** | Spring Boot REST API (Maven) |
| **Server port** | `8080` |
| **Development stage** | **Milestone 1 complete** — Foundation verified. Health endpoint live and tested. |
| **Database** | PostgreSQL (configured via env vars; not required for health endpoint or tests) |
| **Authentication** | Firebase Auth (Milestone 2 — token verification not yet implemented) |

### What the Backend Does (Planned)

The FinTrack backend is a **stateless REST API** serving personal finance data to the Flutter mobile application. It does **not** manage authentication — Firebase Authentication is the sole identity provider. The backend verifies Firebase ID tokens to identify the requesting user.

Planned capabilities:
- Store and retrieve financial transactions (income / expense)
- Aggregate dashboard summary data (balance, monthly totals)
- Manage category budgets
- Track savings goals
- Serve analytics data for charts

**Current reality (Milestone 1):** Application skeleton, security foundation, and health check endpoint only. No financial features.

---

## 2. Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21.0.12 LTS | Application language |
| Spring Boot | **4.1.1** | Application framework (released 2026-08-20) |
| Spring Web (MVC) | (managed by Boot 4.1.1) | REST controllers, embedded Tomcat |
| Spring Data JPA | (managed by Boot 4.1.1) | ORM / repository abstraction |
| Hibernate | (managed by Boot 4.1.1) | JPA implementation |
| Spring Security | (managed by Boot 4.1.1) | Request authorization filter chain |
| Spring Validation | (managed by Boot 4.1.1) | Bean Validation (JSR-380) |
| PostgreSQL Driver | (managed by Boot 4.1.1) | JDBC driver for PostgreSQL |
| Jackson | 3.x (tools.jackson) | JSON serialization — bundled with Boot 4.x |
| Maven | 3.9.x | Build tool |

### Spring Boot 4.x Key Differences from Boot 3.x

Spring Boot 4 (released 2026) introduced significant changes relevant to this project:

| Area | Boot 3.x | Boot 4.x |
|---|---|---|
| Jackson groupId | `com.fasterxml.jackson` | `tools.jackson` (Jackson 3) |
| `@WebMvcTest` package | `org.springframework.boot.test.autoconfigure.web.servlet` | `org.springframework.boot.webmvc.test.autoconfigure` |
| MVC test starter | `spring-boot-starter-test` (bundled) | `spring-boot-starter-webmvc-test` (separate) |
| Security test starter | `spring-security-test` (direct dep) | `spring-boot-starter-security-test` (Boot-managed) |
| Jackson date properties | `spring.jackson.serialization.write-dates-as-timestamps` | Not bound via properties (configure via `JsonMapper` bean) |

---

## 3. Project Structure

```
fintrack_backend/
│
├── pom.xml                          ← Maven build descriptor (Spring Boot 4.1.1, Java 21)
├── mvnw / mvnw.cmd                  ← Maven wrapper scripts
├── PROJECT_STATUS.md                ← This file
│
└── src/
    ├── main/
    │   ├── java/com/fintrack/backend/
    │   │   │
    │   │   ├── FintrackBackendApplication.java   ← @SpringBootApplication entry point
    │   │   │
    │   │   ├── config/
    │   │   │   └── SecurityConfig.java           ← HTTP Security filter chain (stateless, Firebase-ready)
    │   │   │
    │   │   ├── controller/
    │   │   │   └── HealthController.java         ← GET /api/v1/health → 200 UP
    │   │   │
    │   │   ├── dto/
    │   │   │   └── HealthResponse.java           ← Java record: {status, application}
    │   │   │
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java   ← @RestControllerAdvice, consistent error envelopes
    │   │   │
    │   │   ├── security/
    │   │   │   └── package-info.java             ← Placeholder: FirebaseTokenFilter (Milestone 2)
    │   │   │
    │   │   ├── entity/
    │   │   │   └── package-info.java             ← Placeholder: JPA entities (Milestone 3+)
    │   │   │
    │   │   ├── repository/
    │   │   │   └── package-info.java             ← Placeholder: Spring Data repos (Milestone 3+)
    │   │   │
    │   │   ├── service/
    │   │   │   └── package-info.java             ← Placeholder: Business services (Milestone 3+)
    │   │   │
    │   │   └── mapper/
    │   │       └── package-info.java             ← Placeholder: MapStruct mappers (Milestone 3+)
    │   │
    │   └── resources/
    │       └── application.properties            ← App configuration (credentials via env vars)
    │
    └── test/
        └── java/com/fintrack/backend/
            └── HealthControllerTest.java         ← @WebMvcTest slice (Boot 4.x package)
```

---

## 4. Dependencies

### Runtime Dependencies

| Dependency | Artifact ID | Purpose |
|---|---|---|
| Spring Boot Starter Web | `spring-boot-starter-web` | Embedded Tomcat + Spring MVC REST |
| Spring Boot Starter Data JPA | `spring-boot-starter-data-jpa` | JPA / Hibernate ORM |
| Spring Boot Starter Security | `spring-boot-starter-security` | Security filter chain |
| Spring Boot Starter Validation | `spring-boot-starter-validation` | Bean Validation (JSR-380) |
| PostgreSQL Driver | `postgresql` (runtime scope) | JDBC driver |

### Test Dependencies (Spring Boot 4.x modular)

| Dependency | Artifact ID | Purpose |
|---|---|---|
| Spring Boot Starter Test | `spring-boot-starter-test` | JUnit 5, Mockito, AssertJ core |
| Spring Boot WebMVC Test | `spring-boot-starter-webmvc-test` | `@WebMvcTest`, `MockMvc` (Boot 4.x module) |
| Spring Boot Security Test | `spring-boot-starter-security-test` | `@WithAnonymousUser`, `@WithMockUser` (Boot 4.x module) |

### NOT Present (intentionally deferred)

| Package | When to add | Notes |
|---|---|---|
| Firebase Admin SDK | Milestone 2 | `com.google.firebase:firebase-admin:9.x` |
| MapStruct | Milestone 3 | When first entity+DTO pair is created |

---

## 5. How to Run

### Prerequisites

| Requirement | Value |
|---|---|
| Java (JAVA_HOME) | **21** — located at `C:\SDKs\jdk-21.0.12` |
| Maven | 3.9.x (or use `mvnw` wrapper) |
| PostgreSQL | 15+ (only needed to run the full application; **not needed for tests**) |

> **Important:** The system `java` on the dev machine defaults to **Java 8**.
> Always set `JAVA_HOME` explicitly when running Maven commands:
> ```powershell
> $env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
> $env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
> ```

### Running Tests (No Database Required)

Tests use `@WebMvcTest` — no DB connection needed:

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:MAVEN_OPTS = "-Xmx512m -Xms256m"
mvn test --no-transfer-progress
# Expected: BUILD SUCCESS  Tests run: 1, Failures: 0, Errors: 0
```

### Running the Application (PostgreSQL Required)

```powershell
# 1. Set Java 21
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:MAVEN_OPTS = "-Xmx512m -Xms256m"

# 2. Set DB credentials (do NOT hard-code; use env vars)
$env:SPRING_DATASOURCE_URL      = "jdbc:postgresql://localhost:5432/fintrack"
$env:SPRING_DATASOURCE_USERNAME = "fintrack_user"
$env:SPRING_DATASOURCE_PASSWORD = "your_secure_password"

# 3. Start
mvn spring-boot:run --no-transfer-progress
```

### Database Setup (PostgreSQL)

```sql
CREATE DATABASE fintrack;
CREATE USER fintrack_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE fintrack TO fintrack_user;
```

### Starting Without PostgreSQL (Health Check Only)

To smoke-test the health endpoint without a database, exclude JPA/DataSource auto-config:

```powershell
$env:JAVA_HOME = "C:\SDKs\jdk-21.0.12"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH
$env:MAVEN_OPTS = "-Xmx384m -Xms128m"
$env:SPRING_AUTOCONFIGURE_EXCLUDE = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
mvn spring-boot:run --no-transfer-progress
```

Then verify: `GET http://localhost:8080/api/v1/health`

---

## 6. Health Endpoint

**`GET /api/v1/health`**

- **Authentication required:** No — publicly accessible
- **Purpose:** Liveness check; no DB query performed

**Request:**
```
GET http://localhost:8080/api/v1/health
Accept: application/json
```

**Response — 200 OK:**
```json
{
  "status": "UP",
  "application": "FinTrack Backend"
}
```

**Verified:** ✅ Live endpoint confirmed responding correctly on 2026-10-05.

---

## 7. Architecture Overview

```
Flutter App
    │
    │  HTTP + Authorization: Bearer <Firebase ID Token>
    ▼
Spring Boot 4.1.1 (port 8080)
    │
    ├── SecurityFilterChain (stateless, CSRF disabled)
    │       └── [Milestone 2] FirebaseTokenFilter
    │
    ├── @RestController (controller/)     ← HTTP boundary
    │
    ├── @Service (service/)               ← Business logic
    │
    ├── @Repository (repository/)         ← Spring Data JPA
    │
    └── @Entity (entity/)                 ← Domain objects
            │
            ▼
        PostgreSQL
```

### API Versioning

All endpoints are prefixed `/api/v1/`. Breaking changes get a `/api/v2/` prefix.

### Error Envelope (all non-2xx responses)

```json
{
  "timestamp": "2026-10-05T15:17:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "No static resource api/v1/unknown."
}
```

---

## 8. Security Design

### Current State (Milestone 1)

| Setting | Value |
|---|---|
| Session policy | `STATELESS` |
| CSRF | Disabled |
| Public endpoints | `GET /api/v1/health` |
| All other endpoints | `403 Forbidden` (until Firebase filter added) |
| Form login | Disabled |
| HTTP Basic | Disabled |

### Planned State (Milestone 2 — Firebase Token Verification)

1. Add `com.google.firebase:firebase-admin:9.x`
2. Create `FirebaseConfig` — initialize `FirebaseApp` using `GOOGLE_APPLICATION_CREDENTIALS` env var
3. Create `FirebaseTokenFilter extends OncePerRequestFilter` in `security/`
4. Extract `Authorization: Bearer <idToken>`, call `FirebaseAuth.getInstance().verifyIdToken()`
5. Set `UsernamePasswordAuthenticationToken` in `SecurityContext` with Firebase UID as principal
6. Wire filter into `SecurityConfig` before `UsernamePasswordAuthenticationFilter`

**⚠️ Never put service-account JSON content in source files. Use `GOOGLE_APPLICATION_CREDENTIALS` pointing to a file outside the repository.**

---

## 9. Current Status

### ✅ COMPLETED

| Component | Status | Verified |
|---|---|---|
| Maven project (Spring Boot 4.1.1, Java 21) | ✅ Complete | `mvn test` BUILD SUCCESS |
| `pom.xml` — correct Boot 4.x deps | ✅ Complete | Confirmed on Maven Central |
| `application.properties` — credentials via env vars | ✅ Complete | No secrets in source |
| `FintrackBackendApplication` entry point | ✅ Complete | Starts in ~4.6s |
| `SecurityConfig` — stateless, health public | ✅ Complete | Tested via `@WebMvcTest` |
| `HealthController` — GET /api/v1/health | ✅ Complete | Live endpoint verified |
| `HealthResponse` Java record DTO | ✅ Complete | |
| `GlobalExceptionHandler` | ✅ Complete | |
| Package structure (8 packages) | ✅ Complete | |
| `HealthControllerTest` (`@WebMvcTest` Boot 4.x) | ✅ Complete | 1 test, 0 failures |
| **Maven test** | ✅ **BUILD SUCCESS** | `Tests run: 1, Failures: 0, Errors: 0` |
| **Live health endpoint** | ✅ **Verified** | `{"status":"UP","application":"FinTrack Backend"}` |
| Java 21.0.12 | ✅ Confirmed | `C:\SDKs\jdk-21.0.12\bin\java.exe -version` |
| Spring Boot 4.1.1 banner | ✅ Confirmed | Shown in startup log |

### 🔴 NOT STARTED

| Feature | Milestone |
|---|---|
| Firebase token verification | 2 |
| `FirebaseTokenFilter` | 2 |
| `FirebaseConfig` (Admin SDK init) | 2 |
| User profile entity + endpoint | 3 |
| PostgreSQL schema (first migration) | 3 |
| Transaction entity + CRUD API | 4 |
| Dashboard summary endpoint | 5 |
| Category budgets | 6 |
| Savings goals | 6 |
| Analytics endpoints | 7 |
| Flutter ↔ Spring Boot integration | (cross-cutting) |
| CORS configuration | Before Flutter connects |
| Production deployment | Future |

---

## 10. Next Milestones

### Milestone 2 — Firebase Token Verification

**Goal:** Every protected API call authenticates via Firebase ID token.

**Tasks:**
1. Add `com.google.firebase:firebase-admin:9.x` to `pom.xml`
2. Create `config/FirebaseConfig.java` — initialize `FirebaseApp` (read from `GOOGLE_APPLICATION_CREDENTIALS`)
3. Create `security/FirebaseTokenFilter.java extends OncePerRequestFilter`
4. Create `security/FirebasePrincipal.java` record (holds `uid`, `email`, `name`)
5. Wire filter into `SecurityConfig` before `UsernamePasswordAuthenticationFilter`
6. Add `@ExceptionHandler` for `FirebaseAuthException` → 401 in `GlobalExceptionHandler`
7. Test with `@WithMockUser` + mock Firebase in slice test

**Credential rule:** NEVER add service-account JSON to source. Use `GOOGLE_APPLICATION_CREDENTIALS`.

---

### Milestone 3 — User Profile Entity

**Goal:** Authenticated users have a profile in PostgreSQL.

**Tasks:**
1. `UserProfile` entity (`firebaseUid` String PK, `email`, `displayName`, `createdAt`, `updatedAt`)
2. `UserProfileRepository extends JpaRepository<UserProfile, String>`
3. `UserProfileService.getOrCreateProfile(FirebasePrincipal)`
4. `GET /api/v1/me` → `UserProfileResponse` DTO
5. Liquibase or Flyway migration for the `user_profiles` table
6. Switch `spring.jpa.hibernate.ddl-auto` from `validate` to use migrations

---

### Milestone 4 — Transactions CRUD

**Goal:** Flutter can create, read, update, delete transactions.

Fields: `id` (UUID), `userFirebaseUid`, `amount` (BigDecimal), `type` (INCOME/EXPENSE), `category`, `date`, `note`, `createdAt`.

Endpoints: `GET /api/v1/transactions`, `POST`, `PUT /{id}`, `DELETE /{id}`.

---

### Milestone 5 — Dashboard Summary

`GET /api/v1/dashboard` — returns total balance, monthly income/expense totals, 5 most recent transactions. Replaces hardcoded `$12,840.00` in Flutter `home_screen.dart`.

---

### Milestone 6 — Budgets & Savings Goals

Same pattern as Transactions (entity → repository → service → controller → DTO → tests).

---

## 11. Assumptions & Decisions Made

1. **Spring Boot 4.1.1 is the correct version.** Confirmed real and on Maven Central (released 2026-08-20). Previous AI incorrectly assumed it didn't exist.

2. **Spring Boot 4.x testing is modularized.** `@WebMvcTest` moved to `org.springframework.boot.webmvc.test.autoconfigure` and requires `spring-boot-starter-webmvc-test`. Security test annotations require `spring-boot-starter-security-test`. Both confirmed on Maven Central for 4.1.1.

3. **Jackson 3 property binding.** Spring Boot 4.x uses Jackson 3 (`tools.jackson`). The `spring.jackson.serialization.*` property keys do not bind correctly in Boot 4.x. These properties were removed — Jackson 3 defaults are appropriate for a REST API (ISO-8601 dates, etc.). Configure Jackson 3 programmatically via a `JsonMapper` `@Bean` if custom behavior is needed.

4. **Java 21 location.** `C:\SDKs\jdk-21.0.12`. System default Java is 8 — always set `JAVA_HOME` explicitly.

5. **No DB needed for tests or health smoke test.** `@WebMvcTest` slices require no database. The health endpoint also works without PostgreSQL when DataSource auto-config is excluded at startup.

6. **`spring.jpa.hibernate.ddl-auto=validate`** is the correct production-safe default. It prevents accidental schema drift. At Milestone 3, a proper migration tool (Flyway/Liquibase) will be added for schema management.

7. **Firebase is the sole identity provider.** Spring Boot will not issue or manage passwords/tokens — it only verifies Firebase ID tokens. Firebase UID is the stable identity key across the system.

---

## 12. Known Issues & Constraints

| Issue | Severity | Notes |
|---|---|---|
| System Java defaults to Java 8 | 🟡 Dev env | Set `JAVA_HOME=C:\SDKs\jdk-21.0.12` before all Maven commands |
| PostgreSQL required to start app fully | 🟡 Expected | App will fail at startup without DB (DataSource validation). Tests and health smoke test don't need it. |
| `spring.jpa.hibernate.ddl-auto=validate` needs a schema | 🟡 Expected | At Milestone 3, add Flyway/Liquibase. For now, exclude JPA auto-config for no-DB smoke tests. |
| JVM Mockito/byte-buddy warnings during tests | 🟡 Cosmetic | Non-fatal stderr from JVM agent loading. Maven still reports BUILD SUCCESS. |
| No CORS configuration | 🟡 Needed for Flutter | Add `CorsConfigurationSource` bean before Flutter app calls the API. |
| Windows paging errors under memory pressure | 🟡 Dev env | Use `-Xmx512m` via `MAVEN_OPTS`. Avoid `mvn clean` followed by large builds in a single command when memory is low. |
| `spring.jpa.open-in-view` warning | 🟡 Cosmetic | Will be resolved by setting `spring.jpa.open-in-view=false` in a future properties update when JPA entities are added. |

---

## 13. AI Handoff Notes

### Before Modifying Any Code

1. **Read this document fully** before touching any file.
2. **Read `fintrack_app/PROJECT_STATUS.md`** to understand what the Flutter client expects.
3. **Do not add unnecessary dependencies.** Every dep needs a concrete immediate reason.
4. **Never expose credentials** — no passwords, API keys, Firebase service-account JSON content in source or docs.
5. **Do not add `ddl-auto=create` or `create-drop`** to the main properties. Use Flyway/Liquibase (Milestone 3+).
6. **Do not change `SecurityConfig` public endpoint list** without understanding security implications.
7. **Update this document** after completing significant work.

### Spring Boot 4.x Testing Rules

| Annotation | Correct import (Boot 4.x) | Required dependency |
|---|---|---|
| `@WebMvcTest` | `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest` | `spring-boot-starter-webmvc-test` |
| `@AutoConfigureMockMvc` | `org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc` | `spring-boot-starter-webmvc-test` |
| `@WithAnonymousUser` | `org.springframework.security.test.context.support.WithAnonymousUser` | `spring-boot-starter-security-test` |
| `@WithMockUser` | `org.springframework.security.test.context.support.WithMockUser` | `spring-boot-starter-security-test` |

### Package Conventions

| Layer | Package | Annotation |
|---|---|---|
| REST controllers | `com.fintrack.backend.controller` | `@RestController` |
| Business logic | `com.fintrack.backend.service` | `@Service` |
| Data access | `com.fintrack.backend.repository` | (JPA implicit) |
| Domain objects | `com.fintrack.backend.entity` | `@Entity` |
| API contracts | `com.fintrack.backend.dto` | Plain records |
| Object mapping | `com.fintrack.backend.mapper` | `@Mapper` (MapStruct, future) |
| Security filters | `com.fintrack.backend.security` | `@Component` |
| Spring config | `com.fintrack.backend.config` | `@Configuration` |
| Error handling | `com.fintrack.backend.exception` | `@RestControllerAdvice` |

### Jackson 3 (Boot 4.x) Note

Do NOT use `spring.jackson.serialization.*` properties — they don't bind correctly in Boot 4.x. Configure Jackson 3 via a `JsonMapper` `@Bean` in a `@Configuration` class if custom behavior is needed.

---

## 14. Changelog

### 2026-10-05 — Milestone 1: Backend Foundation — VERIFIED COMPLETE

**What existed on disk at resumption:**
- Spring Boot `3.4.5` in `pom.xml` (needed to be updated to 4.1.1)
- All source files present: `SecurityConfig`, `HealthController`, `HealthResponse`, `GlobalExceptionHandler`, `FintrackBackendApplicationTests`, all `package-info.java` files
- `application.properties` with Jackson properties incompatible with Boot 4.x

**Work done in this session:**
- Confirmed Spring Boot 4.1.1 exists on Maven Central (released 2026-08-20)
- Updated `pom.xml`: `3.4.5` → `4.1.1`
- Updated test dependencies to Spring Boot 4.x modular test starters (`spring-boot-starter-webmvc-test`, `spring-boot-starter-security-test`)
- Updated `FintrackBackendApplicationTests.java`: `@WebMvcTest` import updated to Boot 4.x package
- Removed incompatible `spring.jackson.serialization.*` properties from `application.properties`
- **`mvn test` → BUILD SUCCESS** — 1 test, 0 failures, Spring Boot 4.1.1, Java 21.0.12
- **Live health endpoint verified:** `GET /api/v1/health` → `{"status":"UP","application":"FinTrack Backend"}`

**Java:**
- `C:\SDKs\jdk-21.0.12\bin\java.exe -version` confirmed: `java version "21.0.12" 2026-07-21 LTS`
