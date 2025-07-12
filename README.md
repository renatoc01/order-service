# 📝 Order Service

## 📦 Overview

The Order Service handles order creation and shipment events. It produces order events to a Kafka topic for other microservices to consume and react accordingly.

---

## 🐳 Run with Docker

```bash
docker build -t order-service .
docker run -p 8080:8080 order-service
```

The service listens on port 8080 by default.

## 🔗 Kafka Integration

- **Topic:** `orders`  
- **Producer:** `This service sends messages to the orders topic.`

Example event messages:

```java
OrderCreated:{order details...}
OrderShipped:{orderId}
```

## 🧪 Testing

This service includes unit tests for the business logic.

To run the tests:

```bash
./mvnw test
```

## 📂 Directory Structure

```plaintext
order-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/order/
│   │   │   ├── controller/OrderController.java
│   │   │   ├── model/OrderEvent.java
│   │   │   ├── service/OrderService.java
│   │   │   └── store/EventStore.java
│   └── test/
│       └── java/com/example/order/
│   │   │   ├── controller/OrderControllerTest.java
│   │   │   ├── integration/OrderServiceIntegrationTest.java
│   │   │   ├── model/OrderEventTest.java
│   │   │   ├── service/OrderServiceTest.java
│   │   │   └── store/EventStoreTest.java
├── pom.xml
└── README.md
```
## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Kafka**
- **JUnit 5**
- **Docker**

## 🚀 CI/CD Pipeline

- Code Checkout: Pulls the latest source code from the main branch of the repository.
- JDK Setup: Installs Java 17 (Temurin distribution) to build and test the Java application.
- Maven Dependency Cache: Caches Maven dependencies between builds for faster execution.
- Build Permission: Grants execute permission for the Maven wrapper script (./mvnw).
- Build: Runs ./mvnw clean install to compile the code and package it into a JAR.
- Tests: Executes unit and integration tests to verify correctness of the business logic.
- Docker Build: Builds a Docker image tagged as order-service:latest to prepare for containerized deployment.

## 🧪 Smoke Tests in CI/CD

The CI pipeline runs automated smoke tests to verify the basic functionality of the services after deployment, including:

- Creating an order (POST /orders)
- Shipping an order (POST /orders/{id}/ship)
- Fetching order status (GET /orders/{id})

These tests use curl commands to interact with the endpoints and validate the expected responses. Logs from all services are output on test failures for quick debugging.


## 🎯 Observability

We implemented observability across all microservices (order-service, inventory-service, notification-service) using:

- Spring Boot Actuator for health checks, metrics, and tracing endpoints.
- Micrometer with Prometheus integration for metrics collection.
- Prometheus to scrape metrics from services.
- Grafana for visualizing metrics dashboards.
- Zipkin for distributed tracing to analyze request flows across services.

Each service exposes a /actuator/prometheus endpoint for metrics and reports tracing data to Zipkin.

The infrastructure is orchestrated via Docker Compose, which includes Kafka, Zookeeper, Zipkin, Prometheus, and Grafana containers, along with the microservices.

## 🤝 API Documentation with Swagger / OpenAPI

The project includes Swagger UI for interactive API documentation and easier testing.

- The OpenAPI spec is auto-generated using springdoc-openapi.
- Swagger UI is accessible via:
http://localhost:{port}/swagger-ui.html
(replace {port} with the service port, e.g., 8080 for order-service)
- Provides clear, interactive API documentation for all REST endpoints, request bodies, and responses.
