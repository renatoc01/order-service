package com.example.order.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import com.example.order.service.OrderService;

@SpringBootTest
public class OrderServiceIntegrationTest {

    // Usa a imagem Kafka oficial Confluent 7.4.3
    private static KafkaContainer kafkaContainer;

    @Autowired
    private OrderService orderService;

    @BeforeAll
    static void startKafka() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.3"));
        kafkaContainer.start();

        // Define a propriedade que o Spring Boot usa para conectar ao Kafka
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
    }

    @AfterAll
    static void stopKafka() {
        if (kafkaContainer != null) {
            kafkaContainer.stop();
        }
    }

    @Test
    void testCreateAndShipOrder() throws InterruptedException {
        // Cria pedido
        UUID orderId = orderService.createOrder(Map.of("item", "Widget", "quantity", 10));
        assertThat(orderId).isNotNull();

        // Dá um tempinho para o evento ser processado (se necessário)
        Thread.sleep(1000);

        // Verifica estado após criação
        Map<String, Object> stateAfterCreate = orderService.replayState(orderId);
        assertThat(stateAfterCreate.get("status")).isEqualTo("CREATED");

        // Envia evento de envio (shipment)
        orderService.shipOrder(orderId);
        Thread.sleep(1000);

        // Verifica estado após envio
        Map<String, Object> stateAfterShip = orderService.replayState(orderId);
        assertThat(stateAfterShip.get("status")).isEqualTo("SHIPPED");
    }
}
