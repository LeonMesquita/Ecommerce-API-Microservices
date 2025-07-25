package com.leonmesquita.ecommerce.product_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.product_microservice.dtos.rabbitmq.OrderItem;
import com.leonmesquita.ecommerce.product_microservice.models.ProductModel;
import com.leonmesquita.ecommerce.product_microservice.services.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateStockSubscriber {

    @Autowired
    ProductService productService;

    @RabbitListener(
            queues = "${mq.queues.update-stock}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveUptadeStockRequest(@Payload String payload) {
        var mapper = new ObjectMapper();
        try {
            List<OrderItem> dto = mapper.readValue(payload, new com.fasterxml.jackson.core.type.TypeReference<List<OrderItem>>() {});
            productService.updateStock(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // mensagem vai para DLQ
        }
    }
}
