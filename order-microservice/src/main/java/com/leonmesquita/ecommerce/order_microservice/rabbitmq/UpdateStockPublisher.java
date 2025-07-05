package com.leonmesquita.ecommerce.order_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.order_microservice.dtos.rabbitmq.ProductStockRequestDTO;
import com.leonmesquita.ecommerce.order_microservice.models.OrderItemModel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateStockPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Queue updateStockQueue;

    public void updateStock(List<OrderItemModel> dto) throws JsonProcessingException {
        var json = this.convertIntoJson(dto);
        rabbitTemplate.convertAndSend(updateStockQueue.getName(), json);
    }

    private String convertIntoJson(List<OrderItemModel> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }

}
