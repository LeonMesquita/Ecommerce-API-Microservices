package com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    CREATED(1, "CREATED"),
    PAID(2, "PAID"),
    SENT(3, "SENT"),
//    DELIVERED(4, "DELIVERED"),
    CANCELED(4, "CANCELED");

    private final Integer code;
    private final String description;

}
