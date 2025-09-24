package com.ecommerce.store.service;

import com.ecommerce.store.domain.CustomerOrder;
import com.ecommerce.store.domain.OrderItem;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.exception.OrderAlreadyShippedException;
import com.ecommerce.store.exception.OrderNotFoundException;
import com.ecommerce.store.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public CustomerOrder createOrder(CustomerOrder customerOrder) {
        customerOrder.setStatus(OrderStatus.PENDING);
        if (customerOrder.getItems() != null) {
            for (OrderItem item : customerOrder.getItems()) {
                item.setCustomerOrder(customerOrder);
            }
        }
        return orderRepository.save(customerOrder);
    }

    public CustomerOrder getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found for this id"));
    }

    public List<CustomerOrder> getAllOrders(OrderStatus status) {
        return status != null ? orderRepository.findByStatus(status) : orderRepository.findAll();
    }

    @Transactional
    public void cancelOrder(Long id) {
        CustomerOrder customerOrder = orderRepository.findById(id).orElseThrow();
        if (customerOrder.getStatus() == OrderStatus.PENDING) {
            customerOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(customerOrder);
        }else{
            throw new OrderAlreadyShippedException("Cannot cancel an order that is already shipped or processed.");
        }
    }

    @Scheduled(fixedRate = 300000)
    public void updatePendingOrders() {
        List<CustomerOrder> pendingCustomerOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (CustomerOrder customerOrder : pendingCustomerOrders) {
            customerOrder.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(customerOrder);
        }
    }
}