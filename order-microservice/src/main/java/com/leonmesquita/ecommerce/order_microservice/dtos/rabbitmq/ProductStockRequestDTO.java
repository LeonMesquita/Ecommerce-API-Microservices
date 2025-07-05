package com.leonmesquita.ecommerce.order_microservice.dtos.rabbitmq;

import lombok.Data;

@Data
public class ProductStockRequestDTO {
    private int purchasedAmount;
    private Long productId;
}
