package com.example.order.store;

import com.example.order.model.OrderEvent;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EventStore {
	private final Map<UUID, List<OrderEvent>> store = new HashMap<>();

	public void save(OrderEvent event) {
		store.computeIfAbsent(event.getOrderId(), k -> new ArrayList<>()).add(event);
	}

	public List<OrderEvent> getEvents(UUID orderId) {
		return store.getOrDefault(orderId, Collections.emptyList());
	}
}
