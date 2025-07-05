package com.leonmesquita.ecommerce.order_microservice.dtos.feign;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    private Long productId;
    private int amount;
    private BigDecimal unitPrice;
}
