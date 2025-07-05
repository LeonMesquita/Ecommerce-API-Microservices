package com.leonmesquita.ecommerce.cart_microservice.dtos.feign;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private int amount;
    private BigDecimal unitPrice;
}
