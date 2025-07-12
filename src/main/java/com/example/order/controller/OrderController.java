package com.example.order.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Order API", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) {
        UUID id = orderService.createOrder(data);
        return ResponseEntity.ok(id.toString());
    }
    
    @Operation(summary = "Ship an existing order")
    @PostMapping("/{id}/ship")
    public ResponseEntity<Void> shipOrder(@PathVariable UUID id) {
        orderService.shipOrder(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the current state of an order")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.replayState(id));
    }
}
