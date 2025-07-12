package com.leonmesquita.ecommerce.payment_microservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {
    APPROVED(1, "APPROVED"),
    REJECTED(2, "REJECTED");

    private final Integer code;
    private final String description;
}
