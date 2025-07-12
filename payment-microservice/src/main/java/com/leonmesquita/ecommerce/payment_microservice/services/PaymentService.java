package com.leonmesquita.ecommerce.payment_microservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leonmesquita.ecommerce.payment_microservice.clients.OrdersClient;
import com.leonmesquita.ecommerce.payment_microservice.dtos.PaymentRequestDTO;
import com.leonmesquita.ecommerce.payment_microservice.dtos.feign.OrderResponseDTO;
import com.leonmesquita.ecommerce.payment_microservice.dtos.rabbitmq.ApproveOrderRequestDTO;
import com.leonmesquita.ecommerce.payment_microservice.enums.OrderStatusEnum;
import com.leonmesquita.ecommerce.payment_microservice.enums.PaymentStatusEnum;
import com.leonmesquita.ecommerce.payment_microservice.exceptions.GenericBadRequestException;
import com.leonmesquita.ecommerce.payment_microservice.rabbitmq.ApproveOrderPublisher;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    @Autowired
    ApproveOrderPublisher approveOrderPublisher;

    @Autowired
    OrdersClient ordersClient;

    public String payOrder(PaymentRequestDTO dto) {
        OrderResponseDTO order = this.ordersClientFindOrder(dto.getOrderId());
        if (order.getStatus() == OrderStatusEnum.PAID || order.getStatus() == OrderStatusEnum.SENT) {
            throw new GenericBadRequestException("Este pedido já foi pago");
        }
        if (order.getStatus() == OrderStatusEnum.CANCELED) {
            throw new GenericBadRequestException("Este pedido foi cancelado");
        }
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

    public OrderResponseDTO ordersClientFindOrder(Long orderId) {
        try {
            return ordersClient.getOrderById(orderId).getBody();
        } catch (FeignException e) {
            HttpStatus status = HttpStatus.resolve(e.status());
            String message = e.contentUTF8();
            throw new ResponseStatusException(
                    status,
                    message != null && !message.isBlank() ? message : "Erro ao comunicar com microserviço remoto"
            );
        }
    }
}
