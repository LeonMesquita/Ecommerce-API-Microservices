package com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;
    private int amount;
    private Long productId;
    private BigDecimal unitPrice;
}
