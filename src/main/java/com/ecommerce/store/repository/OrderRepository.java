package com.ecommerce.store.repository;

import com.ecommerce.store.domain.CustomerOrder;
import com.ecommerce.store.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByStatus(OrderStatus status);
}