# FinTrack

FinTrack is a personal finance management application built as a full-stack software engineering project. It consists of a Flutter mobile application and a Spring Boot REST API backed by PostgreSQL.

## Overview

The project is designed to allow users to track income and expenses, monitor budgets, and visualize financial data. Development is in early stages: user authentication is complete in the mobile application, and the backend foundation is in place. Financial data features are not yet implemented in either component.

## Architecture

```
Flutter Mobile Application
           |
     HTTP / REST API
           |
  Spring Boot Backend
           |
      PostgreSQL
```

Authentication is handled by Firebase Authentication. The Spring Boot backend is designed to verify Firebase ID tokens on protected routes. Firebase token verification is not yet implemented in the backend.

## Technology Stack

| Component | Technology |
|---|---|
| Mobile application | Flutter 3.x / Dart 3.x |
| Backend | Spring Boot 4.1.1 / Java 21 |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security |
| Authentication | Firebase Authentication |
| Build tool (backend) | Maven |

## Repository Structure

```
system/
├── fintrack_app/        # Flutter mobile application
├── fintrack_backend/    # Spring Boot REST API
├── LICENSE
└── README.md
```

The repository also contains a `design/` directory with UI reference files and a `documentation/` directory with project planning documents. These are not part of the deployable application.

## Current Status

**Flutter application:** Authentication is complete (email/password login, registration, Google Sign-In, password reset). The home screen exists as a placeholder. No financial data screens have been implemented.

**Spring Boot backend:** Project foundation is in place with a working health endpoint. No application business logic, database schema, or API endpoints beyond the health check have been implemented.

See each component's own README and `PROJECT_STATUS.md` for detailed current status.

## Getting Started

### Flutter application

See `fintrack_app/README.md` for setup and run instructions. Requires Flutter SDK and a Firebase project.

### Spring Boot backend

See `fintrack_backend/README.md` for setup and run instructions. Requires Java 21, Maven, and a running PostgreSQL instance.

## Documentation

Each component contains a `PROJECT_STATUS.md` file that serves as the primary developer reference:

- `fintrack_app/PROJECT_STATUS.md` — Flutter application status, architecture decisions, known issues, and development plan
- `fintrack_backend/PROJECT_STATUS.md` — Backend status, package structure, configuration reference, and milestone plan

## License

FinTrack is source-available under a non-commercial license. You may view and study the source code for educational purposes. Commercial use, redistribution, and submission as academic work are not permitted without written permission.

See `LICENSE` for the full terms.

Copyright (c) 2026 Muhammad Umer Nadeem. All rights reserved.
