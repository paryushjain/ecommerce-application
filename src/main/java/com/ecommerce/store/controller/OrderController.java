package com.ecommerce.store.controller;

import com.ecommerce.store.domain.CustomerOrder;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CustomerOrder customerOrder) {
        return ResponseEntity.ok(orderService.createOrder(customerOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerOrder>> getAllOrders(@RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}