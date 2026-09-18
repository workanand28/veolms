# VeoLMS Backend

[![Java](https://img.shields.io/badge/Java-26-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18%2B-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-Database%20Migrations-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org/)
[![JWT](https://img.shields.io/badge/JWT-jjwt%200.12.6-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Backend REST API service for **VeoLMS** (Learning Management System), built with **Spring Boot**, **Spring Security**, **Spring Data JPA**, **PostgreSQL**, and versioned database migrations via **Flyway**.

---

## Table of Contents

- [Features](#features)
- [Architecture & Tech Stack](#architecture--tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Environment Variables](#environment-variables)
  - [Building and Running](#building-and-running)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Error Responses](#error-responses)
- [Database Migrations](#database-migrations)
- [Testing](#testing)
- [Roadmap](#roadmap)

---

## Features

- **Stateless Authentication**: Token-based security with HMAC-SHA signed JSON Web Tokens (JJWT 0.12.x).
- **Spring Security 6 Integration**: Custom `JwtAuthenticationFilter`, DAO authentication provider, stateless session management, and custom authentication entry point returning standardized JSON error payloads.
- **Robust Password Security**: BCrypt password hashing for secure user credential storage.
- **Database Migrations (Flyway)**: Version-controlled database schema migrations executing automatically upon application startup.
- **Data Validation**: Request payload validation using Jakarta Bean Validation annotations (`@Valid`, `@NotBlank`, `@Email`, `@Size`).
- **Centralized Error Handling**: Unified `@RestControllerAdvice` converting application exceptions into consistent `ApiError` representations with field-level validation messages and HTTP status codes.
- **Modular Packaging**: Scalable package structure organized by domain capabilities (`auth`, `user`, `wishlist`, `security`, `config`, `common`).

---

## Architecture & Tech Stack

| Layer / Concern | Technology |
| :--- | :--- |
| **Language** | Java 26 |
| **Framework** | Spring Boot 4.1.1 (Spring Web MVC, Spring Data JPA, Spring Security, Validation) |
| **Database** | PostgreSQL 18+ |
| **Database Migrations** | Flyway (`flyway-core`, `flyway-database-postgresql`) |
| **Security & Cryptography** | Spring Security, Spring Security Crypto (BCrypt), JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) |
| **Build & Dependency Management** | Apache Maven 3.9+ (with Maven Wrapper `mvnw`) |

---

## Project Structure

```text
veolms-backend/
├── .mvn/wrapper/                  # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/veolms/
│   │   │   ├── VeolmsBackendApplication.java   # Application entry point
│   │   │   ├── auth/                           # Authentication domain
│   │   │   │   ├── AuthController.java         # Auth HTTP endpoints (/api/v1/auth)
│   │   │   │   ├── AuthService.java            # Auth business logic & JWT issuance
│   │   │   │   └── dto/                        # Auth DTOs (LoginRequest, LoginResponse)
│   │   │   ├── common/                         # Cross-cutting concerns
│   │   │   │   ├── dto/ApiError.java           # Standard API error representation
│   │   │   │   └── exception/                  # GlobalExceptionHandler (@RestControllerAdvice)
│   │   │   ├── config/                         # Application configuration
│   │   │   │   ├── PasswordConfig.java         # BCrypt PasswordEncoder bean
│   │   │   │   └── SecurityConfig.java         # Spring Security filter chain & endpoints rules
│   │   │   ├── security/                       # Security & token infrastructure
│   │   │   │   ├── CustomUserDetailsService.java # UserDetails loader for Spring Security
│   │   │   │   ├── JwtAuthenticationFilter.java  # Request filter validating Bearer tokens
│   │   │   │   └── JwtService.java             # Token generation, parsing, validation
│   │   │   ├── user/                           # User domain
│   │   │   │   ├── controller/                 # CurrentUserController (/api/v1/users/me)
│   │   │   │   ├── dto/                        # User request/response records
│   │   │   │   ├── entity/User.java            # JPA User entity
│   │   │   │   ├── repository/UserRepository.java # Spring Data JPA repository
│   │   │   │   └── service/                    # User registration & profile services
│   │   │   └── wishlist/                       # Wishlist domain (placeholder for catalog features)
│   │   └── resources/
│   │       ├── application.properties.example  # Configuration template (copy to application.properties)
│   │       └── db/migration/                   # Flyway migration SQL scripts
│   │           ├── V1__create_users.sql
│   │           └── V2__add_password_hash_to_users.sql
│   └── test/
│       └── java/com/veolms/
│           └── VeolmsBackendApplicationTests.java # Context load integration test
├── mvnw / mvnw.cmd                             # Maven wrapper scripts (Linux/macOS & Windows)
├── pom.xml                                     # Project dependencies and build configuration
└── README.md                                   # Project documentation
```

---

## Getting Started

### Prerequisites

Ensure you have the following installed on your machine:
- **Java Development Kit (JDK) 26+**
- **PostgreSQL 18+** (running locally or via container)
- **Git**

### Database Setup

Create the PostgreSQL database for the application:

```sql
CREATE DATABASE veolms;
```

Flyway will automatically create and migrate the required tables on application startup.

### Configuration & Environment Variables

Create your local configuration by copying the example file:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

> Note: `src/main/resources/application.properties` is ignored by Git to keep your local credentials private and secure.

You can configure the application via environment variables or inside your local `application.properties`:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/veolms` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `your_db_password` | Database password |
| `JWT_SECRET` | *(generate your own 256-bit secret)* | Base64-encoded 256-bit secret key for JWT signing |
| `PORT` | `8080` | HTTP port for the backend server |

> **Tip for Production**: Generate a cryptographically secure 256-bit secret key encoded in Base64:
> ```bash
> # Linux / macOS / WSL:
> openssl rand -base64 32
>
> # PowerShell:
> [Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
> ```

### Building and Running

Clone the repository:
```bash
git clone https://github.com/workanand28/veolms.git
cd veolms-backend
```

#### Run with Maven Wrapper

- **Linux / macOS:**
  ```bash
  ./mvnw clean spring-boot:run
  ```

- **Windows (PowerShell or CMD):**
  ```powershell
  .\mvnw.cmd clean spring-boot:run
  ```

The server will start listening at `http://localhost:8080`.

---

## API Endpoints

### Base URL
```text
http://localhost:8080/api/v1
```

### Summary

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Public | Authenticate with credentials and obtain a JWT |
| `GET` | `/api/v1/users/me` | Authenticated (`Bearer <token>`) | Retrieve the authenticated user profile |
| `GET` | `/api/v1/health` | Public | Health status check |

---

### Authentication

#### 1. Login
- **Endpoint:** `POST /api/v1/auth/login`
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "yourPassword123"
  }
  ```
- **Success Response (`200 OK`):**
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzI2Njc5MDAwLCJleHAiOjE3MjY2Nzk5MDB9...",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
  ```

---

### Users

#### 2. Get Current User Profile
- **Endpoint:** `GET /api/v1/users/me`
- **Headers:**
  ```http
  Authorization: Bearer <your_access_token>
  ```
- **Success Response (`200 OK`):**
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "createdAt": "2026-09-18T12:00:00Z"
  }
  ```

---

### Error Responses

The API returns consistent error responses adhering to the `ApiError` schema:

```json
{
  "timestamp": "2026-09-18T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "details": [
    "email: Email must be valid",
    "password: Password is required"
  ]
}
```

Common status codes:
- `400 Bad Request`: Validation failure or malformed JSON payload.
- `401 Unauthorized`: Invalid credentials or missing / expired JWT token.
- `409 Conflict`: Unique constraint violation (e.g. email already exists).
- `500 Internal Server Error`: Unhandled server exception.

---

## Database Migrations

Database schema versioning is managed with **Flyway** in `src/main/resources/db/migration`:

1. **`V1__create_users.sql`**: Creates the `users` table with auto-generated identity ID, `first_name`, `last_name`, unique `email`, and `created_at` timestamp.
2. **`V2__add_password_hash_to_users.sql`**: Adds the `password_hash` column to support secure BCrypt password storage.

---

## Testing

Run unit and integration tests using the Maven wrapper:

```bash
# Run all tests
./mvnw clean test

# Run tests on Windows
.\mvnw.cmd clean test
```

---

## Roadmap

- [ ] Complete public user registration endpoint (`POST /api/v1/auth/register`)
- [ ] Role-Based Access Control (RBAC) (Admin, Instructor, Student)
- [ ] Course Management module (Categories, Modules, Lessons, Materials)
- [ ] Student Enrollment & Wishlist API
- [ ] Swagger / OpenAPI 3 interactive API documentation
- [ ] Docker & Docker Compose setup for localized development and testing

---

## License

This project is licensed under the terms described in the repository.
