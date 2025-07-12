package com.example.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.kafka.bootstrap-servers=localhost:9092",
		"management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans"
	})
class OrderApplicationTests {

	@Test
	void contextLoads() {
	}

}
