package com.ecommerce.store.service;

import com.ecommerce.store.domain.Order;
import com.ecommerce.store.domain.OrderItem;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.exception.OrderAlreadyShippedException;
import com.ecommerce.store.exception.OrderNotFoundException;
import com.ecommerce.store.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        Order order = new Order();
        order.setItems(Collections.singletonList(new OrderItem()));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

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
        Order order = new Order();
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(Collections.singletonList(order));

        List<Order> result = orderService.getAllOrders(OrderStatus.PENDING);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void testGetAllOrdersWithoutStatus() {
        Order order = new Order();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> result = orderService.getAllOrders(null);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testCancelOrder() {
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelOrderThrowsException() {
        Order order = new Order();
        order.setStatus(OrderStatus.SHIPPED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyShippedException.class, () -> orderService.cancelOrder(1L));
    }

}