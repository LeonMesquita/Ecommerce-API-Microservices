package com.leonmesquita.ecommerce.payment_microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leonmesquita.ecommerce.payment_microservice.dtos.PaymentRequestDTO;
import com.leonmesquita.ecommerce.payment_microservice.dtos.rabbitmq.ApproveOrderRequestDTO;
import com.leonmesquita.ecommerce.payment_microservice.enums.PaymentStatusEnum;
import com.leonmesquita.ecommerce.payment_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.payment_microservice.rabbitmq.ApproveOrderPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    @Autowired
    ApproveOrderPublisher approveOrderPublisher;

    public String payOrder(PaymentRequestDTO dto) {
        PaymentStatusEnum status = getRandomStatus();
        if (status == PaymentStatusEnum.REJECTED) {
            throw new GenericBadRequestException("Pagamento rejeitado");
        }
        ApproveOrderRequestDTO approveOrderRequestDTO = new ApproveOrderRequestDTO();
        approveOrderRequestDTO.setOrderId(dto.getOrderId());
        approveOrderRequestDTO.setStatus(PaymentStatusEnum.APPROVED);

        try {
            approveOrderPublisher.approveOrder(approveOrderRequestDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "Pagamento realizado com sucesso";
    }

    private PaymentStatusEnum getRandomStatus() {
        PaymentStatusEnum[] values = PaymentStatusEnum.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}
