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
