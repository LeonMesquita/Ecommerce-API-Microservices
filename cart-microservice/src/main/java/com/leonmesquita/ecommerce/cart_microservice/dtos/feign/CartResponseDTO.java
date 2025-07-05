package com.leonmesquita.ecommerce.cart_microservice.dtos.feign;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private LocalDateTime creationDate;
    private boolean active;
}
