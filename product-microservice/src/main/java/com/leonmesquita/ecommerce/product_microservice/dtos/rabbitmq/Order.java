package com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal total;
    private OrderStatusEnum status;
}
