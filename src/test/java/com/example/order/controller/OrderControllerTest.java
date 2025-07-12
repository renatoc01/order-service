package com.example.order.controller;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.order.service.OrderService;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder_returnsOrderId() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(orderService.createOrder(anyMap())).thenReturn(fakeId);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"item\":\"Widget\",\"quantity\":10}"))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeId.toString()));

        verify(orderService, times(1)).createOrder(anyMap());
    }

    @Test
    void shipOrder_returnsOk() throws Exception {
        UUID fakeId = UUID.randomUUID();
        doNothing().when(orderService).shipOrder(fakeId);

        mockMvc.perform(post("/orders/" + fakeId + "/ship"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).shipOrder(fakeId);
    }

    @Test
    void getOrder_returnsOrderState() throws Exception {
        UUID fakeId = UUID.randomUUID();
        Map<String, Object> fakeState = Map.of("orderId", fakeId.toString(), "status", "shipped");
        when(orderService.replayState(fakeId)).thenReturn(fakeState);

        mockMvc.perform(get("/orders/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(fakeId.toString()))
                .andExpect(jsonPath("$.status").value("shipped"));

        verify(orderService, times(1)).replayState(fakeId);
    }
}
