package com.example.order.store;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.order.service.OrderService;

import org.springframework.kafka.core.KafkaTemplate;

class OrderServiceWithRealStoreTest {

    private EventStore eventStore;
    private KafkaTemplate<String, String> kafka;

    private OrderService orderService;

    @BeforeEach
    void setup() {
        eventStore = new EventStore();
        kafka = Mockito.mock(KafkaTemplate.class);
        orderService = new OrderService(eventStore, kafka);
    }

    @Test
    void createOrder_savesEventToRealStore() {
        Map<String, Object> data = Map.of("item", "Widget", "quantity", 10);

        UUID id = orderService.createOrder(data);

        assertNotNull(id);
        var events = eventStore.getEvents(id);
        assertEquals(1, events.size());
        assertEquals("OrderCreated", events.get(0).getType());
    }

}
