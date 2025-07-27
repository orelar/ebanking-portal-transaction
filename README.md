# E-Banking Portal - Transaction API

This project is a comprehensive solution developed for a backend engineering hiring challenge. It's a secure, reusable RESTful microservice designed to provide paginated transaction histories for an e-banking portal, with data consumed in real-time from a Kafka topic.

## Features

-   **Secure REST API**: Endpoints are secured using JWT Bearer Token authentication via Spring Security.
-   **Paginated Transactions**: Returns a paginated list of transactions for a given month and year.
-   **Dynamic Totals**: Correctly calculates total credit and debit for each page, converted to a user-specified target currency.
-   **Live Kafka Integration**: Consumes transaction data from a Kafka topic in real-time and persists it to the database.
-   **API Documentation**: Interactive API documentation is provided via Swagger UI for easy testing and exploration.
-   **Full Monitoring Stack**: The application is instrumented with Spring Boot Actuator and comes with a Docker Compose setup for a complete Prometheus & Grafana monitoring stack.
-   **Comprehensive Testing**: Includes unit and integration tests covering all application layers.
-   **Containerized**: The entire application stack is containerized using Docker and orchestrated with a single Docker Compose file.

---

## Architecture

The system is designed as a standalone microservice following a layered architecture. It operates within a larger event-driven ecosystem.

**Data Flow:**
1.  **Event Ingestion**: The service's `KafkaListener` subscribes to a `transactions-topic` to consume new transaction events.
2.  **Persistence**: Consumed transactions are saved to a MySQL database, which serves as the queryable read-store.
3.  **API Serving**: A secure REST API, documented with Swagger, exposes endpoints for clients (like an e-Banking frontend) to fetch paginated transaction data.
4.  **Security**: All API requests are authenticated and authorized using JWTs provided by an external Authentication Service.

![Architecture Diagram](images/architecture.png)

---
## Data Model
The core data model for the application is the `Transaction`, which represents a single financial event.

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | String (UUID)| The unique identifier for the transaction. |
| `customerId` | String | The unique key of the customer who owns the transaction. |
| `accountIban`| String | The IBAN of the account involved in the transaction. |
| `amount` | BigDecimal | The monetary value. Negative for debits, positive for credits. |
| `currency` | String | The three-letter currency code (e.g., `CHF`, `GBP`). |
| `valueDate` | LocalDate | The date the transaction took effect. |
| `description`| String | A short description of the transaction. |

---
## Tech Stack

-   **Backend**: Java 17, Spring Boot 3
-   **Data**: Spring Data JPA, MySQL
-   **Messaging**: Spring Kafka
-   **Security**: Spring Security (OAuth2 Resource Server)
-   **API Docs**: springdoc-openapi (Swagger UI)
-   **Testing**: JUnit 5, Mockito, Testcontainers
-   **Monitoring**: Spring Boot Actuator, Prometheus, Grafana
-   **Containerization**: Docker, Docker Compose
-   **CI/CD**: CircleCI (configuration provided)

---

## Getting Started

### Prerequisites
-   Java 17
-   Maven 3.8+
-   Docker & Docker Compose

### 1. Configure the Application
Before running, update the `docker-compose.yml` file with your MySQL database password. Find the `environment` section for the `ebanking-portal` service and set the `SPRING_DATASOURCE_PASSWORD` variable.

### 2. Run with Docker Compose
This single command builds, configures, and runs the entire stack: your application, Kafka, Zookeeper, Prometheus, and Grafana.
```bash
# From the project root directory
docker-compose up --build -d
```
The application will be available at `http://localhost:8080`.

---

## API Documentation & Testing

Interactive API documentation is available via Swagger UI once the application is running.

-   **Swagger UI URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

You can use the **Authorize** button with a sample JWT from [jwt.io](https://jwt.io) to test the secure endpoints.

To populate the database, use the `POST /v1/test/send-transaction` endpoint to send a sample transaction JSON to the Kafka topic. The `TransactionListener` will then process and save it to the database.

---

## Monitoring

-   **Prometheus**: [http://localhost:9090](http://localhost:9090) (Check **Status > Targets** to see if the service is UP)
-   **Grafana**: [http://localhost:3000](http://localhost:3000) (Login: `admin` / `admin`)

---

## Running the Automated Tests

The project includes unit and integration tests that use Testcontainers to provide a live testing environment. To run all tests, execute the following Maven command:
```bash
mvn clean verify
```