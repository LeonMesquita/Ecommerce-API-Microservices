package com.leonmesquita.ecommerce.order_microservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "orderId")
    @JsonIgnore
    private OrderModel order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    BigDecimal unitPrice;
}
