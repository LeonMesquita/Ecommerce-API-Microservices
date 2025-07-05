package com.leonmesquita.ecommerce.product_microservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    @Size(min = 5, max = 100)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Min(value = 1)
    private int stock;

    @NotBlank
    @Size(min = 5, max = 50)
    private String category;
}
