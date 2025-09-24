package com.ecommerce.store.scheduler;

import com.ecommerce.store.domain.Order;
import com.ecommerce.store.domain.OrderStatus;
import com.ecommerce.store.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderProcessor {
    private final OrderRepository orderRepository;

    @Scheduled(fixedRateString = "${order.processing.time.ms}")
    public void updatePendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pendingOrders) {
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
        }
    }
}
