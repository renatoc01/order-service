package com.example.order.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderEventTest {

    @Test
    void shouldCreateOrderEventWithCorrectData() {
        UUID orderId = UUID.randomUUID();
        String type = "OrderCreated";
        String payload = "{\"item\": \"Widget\", \"quantity\": 10}";
        Instant timestamp = Instant.now();

        OrderEvent event = new OrderEvent(orderId, type, payload, timestamp);

        assertEquals(orderId, event.getOrderId());
        assertEquals(type, event.getType());
        assertEquals(payload, event.getPayload());
        assertEquals(timestamp, event.getTimestamp());
    }
}
