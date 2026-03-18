Fruit Inventory Management API (H2)
Description: A RESTful API built with Spring Boot to manage a fruit shop's stock. This project demonstrates the implementation of a full CRUD, the use of the DTO pattern to decouple layers, global exception handling, and containerization using Docker with a multi-stage build.
**************************************************************************************
##📌 Exercise Statement##
The goal is to develop a backend system to register and manage fruit entries (name and weight in kilos). The application uses an H2 in-memory database for rapid development and testing, ensuring high performance and simple configuration.
************************************************************************************
##✨ Features##
Full CRUD Operations: Create, Read, Update, and Delete fruit records.

DTO Pattern: Separation between JPA entities and API data transfer objects.

Data Validation: Input validation using Bean Validation (@NotBlank, @Positive).

Global Exception Handling: Centralized error management returning consistent HTTP status codes (404 for not found, 400 for bad requests).

Dockerized: Optimized production-ready image using multi-stage builds.

Test-Driven Development (TDD): Logic and endpoints verified through automated testing.
*********************************************************************************************
##🛠 Technologies##
Backend: Java 21, Spring Boot 3.x

Database: H2 (In-memory SQL)

Tools: Maven, Lombok, Jakarta Validation

DevOps: Docker (Multi-stage build)
*****************************************************************************
##🚀 Installation & Execution##
1. Clone the repository
Bash
git clone https://github.com/adriaclavero2/4.2-Api-Rest-amb-Spring-boot.git
cd fruit-api-h2/fruit-api-h2
2. Environment Variables
The application is configured to use environment variables for database connectivity. You can override these when running the container:

DB_URL: JDBC URL for H2 (Default: jdbc:h2:mem:fruitdb)

DB_USER: Database username (Default: sa)

DB_PASS: Database password (Default: empty)

3. Running the Application
Option A: Using Docker (Recommended)
Build the image and run the container:

Bash
docker build -t fruit-app .
docker run -p 8080:9000 fruit-app
Note: The app runs on port 9000 inside the container and is mapped to 8080 on your host.

Option B: Using Maven

Bash
./mvnw spring-boot:run
4. Testing
To execute the automated test suite (TDD):

Bash
./mvnw test
*****************************************************************
##📸 Demo##
Once the application is running, you can access the API at:
GET http://localhost:8080/fruits
*********************************************************************
##🧩 Diagrams & Technical Decisions##
Multi-Stage Docker Build: We used a two-stage Dockerfile. Stage 1 (Build) compiles the code using Maven and OpenJDK 21. Stage 2 (Run) uses a lightweight JRE image to keep the final production image small and secure.

Layered Architecture (MVC):

Controller: Manages HTTP requests and returns ResponseEntity.

Service: Contains business logic and interacts with the Mapper.

Repository: Handles H2 database persistence via Spring Data JPA.

Model/DTO: Ensures that internal database structures (Entities) are not exposed directly to the client.
