package com.example.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.order.model.OrderEvent;
import com.example.order.store.EventStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.kafka.core.KafkaTemplate;

class OrderServiceTest {

    @Mock
    private EventStore store;

    @Mock
    private KafkaTemplate<String, String> kafka;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_savesEventAndSendsKafkaMessage() {
        Map<String, Object> data = Map.of("item", "Widget", "quantity", 10);

        UUID id = orderService.createOrder(data);

        // Verifica se o ID é gerado
        assertNotNull(id);

        // Verifica se save foi chamado com evento do tipo OrderCreated
        ArgumentCaptor<OrderEvent> eventCaptor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(store, times(1)).save(eventCaptor.capture());

        OrderEvent savedEvent = eventCaptor.getValue();
        assertEquals(id, savedEvent.getOrderId());
        assertEquals("OrderCreated", savedEvent.getType());
        assertTrue(savedEvent.getPayload().contains("Widget")); // payload é toString do map

        // Verifica se kafka.send foi chamado com tópico "orders" e mensagem correta
        verify(kafka, times(1)).send(eq("orders"), contains("OrderCreated"));
    }

    @Test
    void shipOrder_savesEventAndSendsKafkaMessage() {
        UUID id = UUID.randomUUID();

        orderService.shipOrder(id);

        ArgumentCaptor<OrderEvent> eventCaptor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(store, times(1)).save(eventCaptor.capture());

        OrderEvent savedEvent = eventCaptor.getValue();
        assertEquals(id, savedEvent.getOrderId());
        assertEquals("OrderShipped", savedEvent.getType());

        verify(kafka, times(1)).send(eq("orders"), contains("OrderShipped"));
    }

    @Test
    void replayState_returnsCorrectState() {
        UUID id = UUID.randomUUID();

        OrderEvent created = new OrderEvent(id, "OrderCreated", "{\"item\":\"Widget\"}", Instant.now());
        OrderEvent shipped = new OrderEvent(id, "OrderShipped", "", Instant.now());

        when(store.getEvents(id)).thenReturn(List.of(created, shipped));

        Map<String, Object> state = orderService.replayState(id);

        assertEquals("SHIPPED", state.get("status"));
        assertEquals("{\"item\":\"Widget\"}", state.get("details"));
    }

    @Test
    void replayState_emptyEvents_returnsEmptyState() {
        UUID id = UUID.randomUUID();
        when(store.getEvents(id)).thenReturn(List.of());

        Map<String, Object> state = orderService.replayState(id);

        assertTrue(state.isEmpty());
    }
}
