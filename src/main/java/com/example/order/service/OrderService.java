package com.example.order.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order.model.OrderEvent;
import com.example.order.store.EventStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final EventStore store;
    private final KafkaTemplate<String, String> kafka;

    public UUID createOrder(Map<String, Object> data) {
        UUID id = UUID.randomUUID();
        OrderEvent event = new OrderEvent(id, "OrderCreated", data.toString(), Instant.now());
        store.save(event);
        kafka.send("orders", event.getType() + ":" + event.getPayload());
        return id;
    }

    public void shipOrder(UUID id) {
        OrderEvent event = new OrderEvent(id, "OrderShipped", "", Instant.now());
        store.save(event);
        kafka.send("orders", event.getType() + ":" + id);
    }

    public Map<String, Object> replayState(UUID id) {
        List<OrderEvent> events = store.getEvents(id);
        Map<String, Object> state = new HashMap<>();
        for (OrderEvent e : events) {
            if (e.getType().equals("OrderCreated")) {
                state.put("status", "CREATED");
                state.put("details", e.getPayload());
            } else if (e.getType().equals("OrderShipped")) {
                state.put("status", "SHIPPED");
            }
        }
        return state;
    }
}
