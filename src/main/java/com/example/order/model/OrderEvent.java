package com.example.order.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderEvent {
	
    private UUID orderId;
    private String type; // e.g., OrderCreated, OrderShipped
    private String payload;
    private Instant timestamp = Instant.now();

}