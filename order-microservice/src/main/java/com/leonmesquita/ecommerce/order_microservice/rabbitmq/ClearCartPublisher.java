package com.leonmesquita.ecommerce.order_microservice.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClearCartPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

//    @Autowired
//    Queue clearCartQueue;

    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;


    public void clearCart(Long cartId) {
        rabbitTemplate.convertAndSend(clearCartQueue, cartId);
    }

}
