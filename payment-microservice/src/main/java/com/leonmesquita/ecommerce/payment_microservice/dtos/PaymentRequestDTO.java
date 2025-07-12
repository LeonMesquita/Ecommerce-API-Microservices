package com.leonmesquita.ecommerce.payment_microservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    @NotNull
    private Long orderId;
}
