package com.leonmesquita.ecommerce.order_microservice.dtos.rabbitmq;

import com.leonmesquita.ecommerce.order_microservice.models.enums.PaymentStatusEnum;
import lombok.Data;

@Data
public class ApproveOrderRequestDTO {
    private Long orderId;
    private PaymentStatusEnum status;
}
