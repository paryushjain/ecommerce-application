package com.ecommerce.store.controller;

import com.ecommerce.store.domain.CustomerOrder;
import com.ecommerce.store.domain.OrderItem;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_ReturnsCreatedOrder() throws Exception {
        CustomerOrder sampleOrder = createSampleOrder();
        when(orderService.createOrder(any(CustomerOrder.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    private CustomerOrder createSampleOrder() {
        CustomerOrder order = CustomerOrder.builder().id(1L).status(OrderStatus.PENDING).build();
        OrderItem item = OrderItem.builder().customerOrder(order).productName("PROD-101").quantity(1).build();
        order.setItems(Collections.singletonList(item));
        return order;
    }


}