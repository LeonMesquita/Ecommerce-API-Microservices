package com.leonmesquita.ecommerce.order_microservice.dtos.feign;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private LocalDateTime creationDate;
    private boolean active;
}
