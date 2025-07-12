package com.leonmesquita.ecommerce.payment_microservice.dtos.rabbitmq;

import com.leonmesquita.ecommerce.payment_microservice.enums.PaymentStatusEnum;
import lombok.Data;

@Data
public class ApproveOrderRequestDTO {
    private Long orderId;
    private PaymentStatusEnum status;
}
