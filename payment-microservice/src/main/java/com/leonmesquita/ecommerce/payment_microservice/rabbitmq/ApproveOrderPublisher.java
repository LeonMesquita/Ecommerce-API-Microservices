package com.leonmesquita.ecommerce.payment_microservice.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.payment_microservice.dtos.rabbitmq.ApproveOrderRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApproveOrderPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.update-status}")
    private String updateStatus;


    public void approveOrder(ApproveOrderRequestDTO dto) throws JsonProcessingException {
        var json = this.convertIntoJson(dto);
        rabbitTemplate.convertAndSend(updateStatus, json);
    }

    private String convertIntoJson(ApproveOrderRequestDTO data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
