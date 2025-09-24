package com.ecommerce.store.scheduler;

import com.ecommerce.store.domain.Order;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderProcessorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderProcessor orderProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdatePendingOrders() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Collections.singletonList(order));

        orderProcessor.updatePendingOrders();

        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

}