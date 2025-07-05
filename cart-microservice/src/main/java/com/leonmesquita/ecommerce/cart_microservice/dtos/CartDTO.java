package com.leonmesquita.ecommerce.cart_microservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDTO {
    @NotNull
    private Long userId;
}
