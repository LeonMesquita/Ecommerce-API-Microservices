package com.leonmesquita.ecommerce.order_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.order_microservice.dtos.rabbitmq.ApproveOrderRequestDTO;
import com.leonmesquita.ecommerce.order_microservice.models.enums.PaymentStatusEnum;
import com.leonmesquita.ecommerce.order_microservice.services.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UpdateStockSubscriber {

    @Autowired
    OrderService orderService;

    @RabbitListener(queues = "${mq.queues.update-status}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveClearCartRequest(@Payload String payload) {
        var mapper = new ObjectMapper();
        try {
            ApproveOrderRequestDTO dto = mapper.readValue(payload, ApproveOrderRequestDTO.class);
            orderService.approveOrder(dto.getOrderId(), dto.getStatus());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // mensagem vai para DLQ
        }

    }
}
