package com.leonmesquita.ecommerce.order_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.order_microservice.models.OrderModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.update-stock}")
    private String updateStockQueue;

    public void updateStock(OrderModel dto) throws JsonProcessingException {
        var json = this.convertIntoJson(dto);
        rabbitTemplate.convertAndSend(updateStockQueue, json);
    }



    private String convertIntoJson(OrderModel data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
