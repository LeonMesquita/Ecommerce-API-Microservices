package com.leonmesquita.ecommerce.order_microservice.models.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    CREATED(1, "CREATED"),
    PAID(2, "PAID"),
    SENT(3, "SENT"),
    CANCELED(4, "CANCELED");

    private final Integer code;
    private final String description;

}
