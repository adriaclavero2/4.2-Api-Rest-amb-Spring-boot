Aquí está para copiar y pegar directamente:

---

```markdown
<div align="center">

# 🍎 Fruit & Provider Inventory API

**A production-ready RESTful API built with Spring Boot and MySQL**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

[Features](#-features) · [Architecture](#-architecture) · [Getting Started](#-getting-started) · [API Reference](#-api-reference) · [Testing](#-testing)

</div>

---

## 📖 Overview

A fully containerized RESTful API for managing a fruit shop's inventory and its supplier network. Built following clean architecture principles with a layered design, the DTO pattern, relational data modeling via JPA, and a complete TDD testing suite.

> **Exercise Level 2** — Introduces `@ManyToOne` JPA relationships, global exception handling, Docker multi-stage builds, and Bean Validation.

---

## ✨ Features

| Feature | Details |
|---|---|
| **Relational CRUD** | Full Create / Read / Update / Delete for `Fruit` and `Provider` entities |
| **JPA Relationships** | `@ManyToOne` linking each fruit to its provider; referential integrity enforced |
| **DTO Pattern** | Strict separation between JPA entities and API transfer objects |
| **Bean Validation** | `@NotBlank`, `@Positive`, `@NotNull` on all inputs |
| **Global Exception Handling** | Centralized `@RestControllerAdvice` with consistent HTTP responses |
| **Dockerized** | Multi-stage build + Docker Compose for zero-config local setup |
| **TDD** | Controller and Service layers covered with Mockito + MockMvc |

### Exception Mapping

| HTTP Status | Scenario |
|---|---|
| `200 OK` | Successful read or update |
| `201 Created` | Resource created successfully |
| `400 Bad Request` | Validation errors or constraint conflicts |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate entries (e.g., existing provider name) |

---

## 🏛 Architecture

```
src/
├── controller/        # HTTP layer — handles requests, returns ResponseEntity
├── service/           # Business logic — relational integrity, validation rules
├── repository/        # Data access — Spring Data JPA interfaces
├── model/             # JPA Entities (Fruit, Provider)
├── dto/               # Data Transfer Objects (request/response decoupling)
└── exception/         # Global exception handler + custom exceptions
```

### JPA Relationship

```
Provider  ──── 1 ────< Fruit
  (id, name)           (id, name, quantity, price, provider_id)
```

Each `Fruit` must reference a valid `Provider`. Deleting a `Provider` that still has associated fruits returns `400 Bad Request`.

### Docker Multi-Stage Build

```dockerfile
# Stage 1 — Build
FROM maven:3.9-eclipse-temurin-21 AS build
# Compiles source, produces .jar

# Stage 2 — Run
FROM eclipse-temurin:21-jre-alpine
# Copies only the .jar → lean, secure production image
```

Docker Compose links the Spring Boot container to a MySQL 8.0 container via an internal Docker network.

---

## 🚀 Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (recommended)
- Or: Java 21 + Maven + local MySQL 8.0

### 1. Clone the repository

```bash
git clone https://github.com/adriaclavero2/4.2-Api-Rest-amb-Spring-boot.git
cd fruit-api-h2
```

### 2. Environment Variables

The following variables are preconfigured in `docker-compose.yml`. Override as needed:

| Variable | Default | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://db:3306/fruit_db` | JDBC connection URL |
| `SPRING_DATASOURCE_USERNAME` | `root` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `root` | Database password |

### 3. Run the application

**Option A — Docker Compose (Recommended) 🐳**

No local Java or MySQL installation required. Builds the image and provisions the database automatically.

```bash
docker compose up -d --build
```

The API will be available at `http://localhost:8080`.  
MySQL will be exposed on port `3306`.

**Option B — Maven (requires local MySQL) ☕**

Ensure a local MySQL server is running with a database named `fruit_db`, then:

```bash
./mvnw spring-boot:run
```

---

## 📡 API Reference

### Providers

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/providers` | Create a new provider |
| `GET` | `/providers` | List all providers |
| `PUT` | `/providers/{id}` | Update an existing provider |
| `DELETE` | `/providers/{id}` | Delete a provider *(fails if fruits exist)* |

### Fruits

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/fruits` | Create a fruit *(requires valid `providerId`)* |
| `GET` | `/fruits` | List all fruits |
| `GET` | `/fruits?providerId={id}` | List fruits by provider |
| `GET` | `/fruits/{id}` | Get a specific fruit |
| `PUT` | `/fruits/{id}` | Update a fruit |
| `DELETE` | `/fruits/{id}` | Delete a fruit |

### Example Request — Create a Fruit

```bash
curl -X POST http://localhost:8080/fruits \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mango",
    "quantity": 50,
    "price": 1.99,
    "providerId": 1
  }'
```

```json
{
  "id": 3,
  "name": "Mango",
  "quantity": 50,
  "price": 1.99,
  "providerName": "Tropical Imports S.L."
}
```

---

## 🧪 Testing

The project follows a **Test-Driven Development (TDD)** approach, covering both the Controller and Service layers.

```bash
./mvnw test
```

| Layer | Tool | Scope |
|---|---|---|
| Controller | `MockMvc` | HTTP request/response, status codes |
| Service | `Mockito` | Business logic, mock repository calls |

---

## 🛠 Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Database:** MySQL 8.0 + Spring Data JPA + Hibernate
- **Build:** Apache Maven + Lombok + Jakarta Validation
- **DevOps:** Docker, Docker Compose (multi-stage build)
- **Testing:** JUnit 5, Mockito, MockMvc

---

## 🤝 Contributing

Contributions, issues and feature requests are welcome.

1. Fork the project
2. Create your branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'feat: add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---

<div align="center">

Made with ☕ and Spring Boot

</div>
```
