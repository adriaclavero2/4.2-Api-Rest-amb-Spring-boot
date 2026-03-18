# 🍎 Fruit Inventory Management API (H2)

**Description**: A RESTful API built with Spring Boot to manage a fruit shop's stock. This project demonstrates the implementation of a full CRUD, the use of the DTO pattern to decouple layers, global exception handling, and containerization using Docker with a multi-stage build.

---

## 📌 Exercise Statement
The goal is to develop a backend system to register and manage fruit entries (name and weight in kilos). The application uses an **H2 in-memory database** for rapid development and testing, ensuring high performance and simple configuration.

---

## ✨ Features
* **Full CRUD Operations**: Create, Read, Update, and Delete fruit records.
* **DTO Pattern**: Separation between JPA entities and API data transfer objects for better security and architecture.
* **Data Validation**: Input validation using Bean Validation (`@NotBlank`, `@Positive`).
* **Global Exception Handling**: Centralized error management returning consistent HTTP status codes:
    * `404 Not Found` for missing resources.
    * `400 Bad Request` for validation errors.
* **Dockerized**: Optimized production-ready image using **multi-stage builds**.
* **Test-Driven Development (TDD)**: Logic and endpoints verified through automated testing suite.

---

## 🛠 Technologies
* **Backend**: Java 21, Spring Boot 3.x
* **Database**: H2 (In-memory SQL)
* **Tools**: Maven, Lombok, Jakarta Validation
* **DevOps**: Docker (Multi-stage build)

---

## 🚀 Installation & Execution

### 1. Clone the repository
```bash
git clone https://github.com/adriaclavero2/4.2-Api-Rest-amb-Spring-boot.git
cd fruit-api-h2/fruit-api-h2
