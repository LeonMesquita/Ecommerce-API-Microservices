package com.leonmesquita.ecommerce.payment_microservice.dtos.feign;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private int amount;
    private BigDecimal unitPrice;
}
