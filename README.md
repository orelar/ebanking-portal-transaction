# E-Banking Portal - Backend Challenge

This project is a comprehensive solution developed for a backend engineering hiring challenge. It's a secure, reusable RESTful microservice designed to provide paginated transaction histories for an e-banking portal, with data consumed in real-time from a Kafka topic.

## Features

-   **Secure REST API**: Endpoints are secured using JWT Bearer Token authentication.
-   **Paginated Transactions**: Returns a paginated list of transactions for a given month and year.
-   **Dynamic Totals**: Correctly calculates total credit and debit for each page, converted to a user-specified target currency.
-   **Live Kafka Integration**: Consumes transaction data from a Kafka topic and persists it to the database.
-   **API Documentation**: API endpoints are manually documented below.
-   **Comprehensive Testing**: Includes unit and integration tests covering all application layers.
-   **Containerized**: The application and its dependencies (Kafka, MySQL) can be run with a single Docker Compose command.
-   **Monitoring Ready**: Instrumented with Spring Boot Actuator to expose production-ready metrics.

---

## Architecture

The system is a standalone microservice with a layered architecture, designed to operate within an event-driven ecosystem.

**Data Flow:**
1.  **Event Ingestion**: The `KafkaListener` subscribes to a `transactions-topic` to consume new transaction events.
2.  **Persistence**: Transactions are saved to a MySQL database, which serves as the queryable read-store.
3.  **API Serving**: A secure REST API exposes endpoints for clients to fetch transaction data.
4.  **Security**: API requests are authenticated and authorized using JWTs.

![Architecture Diagram](images/architecture.png)

---

## Data Model
The core data model is the `Transaction`, which represents a single financial event.

| Field         | Type         | Description                                        |
| :------------ | :----------- | :------------------------------------------------- |
| `id`          | String (UUID)| The unique identifier for the transaction.         |
| `customerId`  | String       | The unique key of the customer who owns the transaction. |
| `accountIban` | String       | The IBAN of the account involved in the transaction. |
| `amount`      | BigDecimal   | The monetary value. Negative for debits, positive for credits. |
| `currency`    | String       | The three-letter currency code (e.g., `CHF`, `GBP`).   |
| `valueDate`   | LocalDate    | The date the transaction took effect.              |
| `description` | String       | A short description of the transaction.            |

---

## Tech Stack

-   **Backend**: Java 17, Spring Boot 3
-   **Data**: Spring Data JPA, MySQL
-   **Messaging**: Spring Kafka
-   **Security**: Spring Security (OAuth2 Resource Server)
-   **Testing**: JUnit 5, Mockito, Testcontainers
-   **Monitoring**: Spring Boot Actuator, Prometheus, Grafana
-   **Containerization**: Docker, Docker Compose
-   **CI/CD**: CircleCI

---

## Getting Started

### Prerequisites
-   Java 17
-   Maven 3.8+
-   Docker & Docker Compose

### 1. Configure
Before running, update the `docker-compose.yml` file with your MySQL password in the `environment` section for both the `db` and `ebanking-portal` services.

### 2. Run
This single command builds and runs the entire stack (application, Kafka, MySQL, Prometheus, Grafana).
```bash
docker-compose up --build -d
```
The application will be available at `http://localhost:8080`.

---

## API Endpoints
A tool like Postman is recommended for testing. All endpoints require a Bearer Token for authorization.

### Get Monthly Transactions
-   **URL:** `/v1/transactions`
-   **Method:** `GET`
-   **Query Parameters:**

    | Name           | Type   | Description                                            |
    | :------------- | :----- | :----------------------------------------------------- |
    | `year`         | int    | The calendar year                                      |
    | `month`        | int    | The calendar month (1-12)                              |
    | `targetCurrency`| String | The currency for totals (e.g., USD)                    |
    | `page`         | int    | The page number (starts at 0)                          |
    | `size`         | int    | The number of items per page                           |
    | `sort`         | String | The sort criteria, e.g., `valueDate,desc`              |

### Get Transaction by ID
-   **URL:** `/v1/transactions/{transactionId}`
-   **Method:** `GET`

### [Test] Send a Transaction
-   **URL:** `/v1/test/send-transaction`
-   **Method:** `POST`
-   **Body (JSON):** A `Transaction` object.

---

## Running Automated Tests

The project includes a full suite of unit and integration tests that use Testcontainers to provide a live testing environment.
```bash
mvn clean verify
```

---

## Project Showcase

### Successful CircleCI Build
The CI/CD pipeline automatically runs all unit and integration tests on every commit.
![CircleCI Success](images/circle-ci.png)

### Docker Desktop Stack
The `docker-compose.yml` file orchestrates the entire application stack.
![Docker Desktop](images/docker-desktop.png)

### Prometheus Monitoring
Prometheus successfully scraping metrics from the running application.
![Prometheus Targets](images/prometheus-targets.png)

### Grafana Dashboard
A simple Grafana dashboard visualizing live JVM metrics from the application.
![Grafana Dashboard](images/grafana-dashboard.png)