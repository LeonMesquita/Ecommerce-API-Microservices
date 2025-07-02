package com.leonmesquita.ecommerce.auth_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.auth_microservice.dtos.rabbitmq.DataCartRequestDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateCartPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    Queue queueCreateCart;

    public void createCart(DataCartRequestDTO data) throws JsonProcessingException  {
        var json = this.convertIntoJson(data);
        rabbitTemplate.convertAndSend(queueCreateCart.getName(), json);
    }

    private String convertIntoJson(DataCartRequestDTO data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
