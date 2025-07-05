package com.leonmesquita.ecommerce.cart_microservice.dtos.feign;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
}
