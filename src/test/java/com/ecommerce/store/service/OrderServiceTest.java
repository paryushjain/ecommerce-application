package com.ecommerce.store.service;

import com.ecommerce.store.domain.CustomerOrder;
import com.ecommerce.store.domain.OrderItem;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.exception.OrderAlreadyShippedException;
import com.ecommerce.store.exception.OrderNotFoundException;
import com.ecommerce.store.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        CustomerOrder order = new CustomerOrder();
        order.setItems(Collections.singletonList(new OrderItem()));

        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(order);

        CustomerOrder result = orderService.createOrder(order);

        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrderById() {
        CustomerOrder order = new CustomerOrder();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        CustomerOrder result = orderService.getOrderById(1L);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByIdThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testGetAllOrdersWithStatus() {
        CustomerOrder order = new CustomerOrder();
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Collections.singletonList(order));

        List<CustomerOrder> result = orderService.getAllOrders(OrderStatus.PENDING);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetAllOrdersWithoutStatus() {
        CustomerOrder order = new CustomerOrder();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<CustomerOrder> result = orderService.getAllOrders(null);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testCancelOrder() {
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelOrderThrowsException() {
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyShippedException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void testUpdatePendingOrders() {
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Collections.singletonList(order));

        orderService.updatePendingOrders();

        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }
}