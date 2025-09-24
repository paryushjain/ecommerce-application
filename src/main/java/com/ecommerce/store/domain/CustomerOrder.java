package com.ecommerce.store.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String orderType;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerOrder", orphanRemoval = true)
    private List<OrderItem> items;
}