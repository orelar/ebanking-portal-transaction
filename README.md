# E-Banking Portal - Backend Challange

This project is a comprehensive solution developed for a backend engineering hiring challenge. It's a secure, reusable RESTful microservice designed to provide paginated transaction histories for an e-Banking portal, with data consumed in real-time from a Kafka topic.

## Features

-   **Secure REST API**: Endpoints are secured using JWT Bearer Token authentication via Spring Security.
-   **Paginated Transactions**: Returns a paginated list of transactions for a given month and year.
-   **Dynamic Totals**: Correctly calculates total credit and debit for each page, converted to a user-specified target currency.
-   **Live Kafka Integration**: Consumes transaction data from a Kafka topic in real-time and persists it to the database.
-   **API Documentation**: Interactive API documentation is provided via Swagger UI for easy testing and exploration.
-   **Full Monitoring Stack**: The application is instrumented with Spring Boot Actuator and comes with a Docker Compose setup for a complete Prometheus & Grafana monitoring stack.
-   **Comprehensive Testing**: Includes unit tests for services and integration tests for the controller and repository layers.
-   **Containerized**: The entire application stack (App, Kafka, Zookeeper, Prometheus, Grafana) is containerized using Docker and orchestrated with a single Docker Compose file.

---

## Architecture

The system is designed as a standalone microservice following a layered architecture. It operates within a larger event-driven ecosystem.

**Data Flow:**
1.  **Event Ingestion**: The service's `KafkaListener` subscribes to a `transactions-topic` to consume new transaction events in real-time.
2.  **Persistence**: Consumed transactions are saved to a MySQL database, which serves as the queryable read-store.
3.  **API Serving**: A secure REST API, documented with Swagger, exposes endpoints for clients (like an e-Banking frontend) to fetch paginated transaction data.
4.  **Security**: All API requests are authenticated and authorized using JWTs provided by an external Authentication Service.

![Architecture Diagram](images/architecture.png)

---

## Tech Stack

-   **Backend**: Java 17, Spring Boot 3
-   **Data**: Spring Data JPA, MySQL
-   **Messaging**: Spring Kafka
-   **Security**: Spring Security (OAuth2 Resource Server)
-   **API Docs**: springdoc-openapi (Swagger UI)
-   **Testing**: JUnit 5, Mockito
-   **Monitoring**: Spring Boot Actuator, Prometheus, Grafana
-   **Containerization**: Docker, Docker Compose
-   **CI/CD**: CircleCI (configuration provided)

---

## How to Run

### Prerequisites
-   Java 17
-   Maven 3.8+
-   Docker & Docker Compose

### 1. Configure the Application
Before running, update the `docker-compose.yml` file with your MySQL database password. Find the `environment` section for the `ebanking-portal` service and set the `SPRING_DATASOURCE_PASSWORD` variable.

### 2. Run with Docker Compose (Recommended)
This single command will build, configure, and run the entire stack: your application, Kafka, Zookeeper, Prometheus, and Grafana.

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

To populate the database with data, use the `POST /v1/test/send-transaction` endpoint to send a sample transaction JSON to the Kafka topic. The `TransactionListener` will then process and save it to the database.

---

## Monitoring

-   **Prometheus**: [http://localhost:9090](http://localhost:9090) (Check **Status > Targets** to see if the service is UP)
-   **Grafana**: [http://localhost:3000](http://localhost:3000) (Login: **admin** / **admin**)

---

## Testing

The project includes unit and integration tests covering the service, controller, and repository layers. To run all tests, execute the following Maven command:
```bash
mvn clean verify
```