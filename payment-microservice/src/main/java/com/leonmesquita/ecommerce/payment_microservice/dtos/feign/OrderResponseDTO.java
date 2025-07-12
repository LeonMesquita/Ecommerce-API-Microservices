package com.leonmesquita.ecommerce.payment_microservice.dtos.feign;

import com.leonmesquita.ecommerce.payment_microservice.enums.OrderStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private BigDecimal total;
    private OrderStatusEnum status;
    private List<OrderItemDTO> items;
}
