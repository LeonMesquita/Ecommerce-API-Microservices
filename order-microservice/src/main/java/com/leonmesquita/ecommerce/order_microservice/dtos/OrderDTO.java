package com.leonmesquita.ecommerce.order_microservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDTO {
    @NotNull
    private Long cartId;
}
