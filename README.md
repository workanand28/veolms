# VeoLMS Backend

Backend service for VeoLMS built with Spring Boot, Spring Security, JWT authentication, Spring Data JPA, PostgreSQL, and Flyway database migrations.

## Tech Stack
- **Java 26**
- **Spring Boot 4.x** (Web, Data JPA, Security, Validation)
- **PostgreSQL**
- **Flyway** (Database Migrations)
- **JWT (jjwt)** (Token-based Authentication)

## Getting Started

### Prerequisites
- Java 26+
- PostgreSQL 18+
- Maven 3.9+

### Configuration
Update `src/main/resources/application.properties` with your database credentials or set the corresponding environment variables:
- `DB_URL` (default: `jdbc:postgresql://localhost:5432/veolms`)
- `DB_USERNAME` (default: `postgres`)
- `DB_PASSWORD` (default: `Anand2026`)
- `JWT_SECRET` (Base64-encoded secret, at least 32 bytes)

### Build & Run
```bash
# Compile and test
mvn clean test

# Run application
mvn spring-boot:run
```
