package com.leonmesquita.ecommerce.cart_microservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonmesquita.ecommerce.cart_microservice.dtos.CartDTO;
import com.leonmesquita.ecommerce.cart_microservice.models.CartModel;
import com.leonmesquita.ecommerce.cart_microservice.services.CartService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CreateCartSubscriber {
    @Value("${mq.queues.create-cart}")
    private String createCartQueue;

    @Autowired
    CartService cartService;

    @RabbitListener(queues = "create-cart")
    public void receiveCreateCartRequest(@Payload String payload) {
        try {
            var mapper = new ObjectMapper();
            CartDTO dto = mapper.readValue(payload, CartDTO.class);
            CartModel cart = cartService.createCart(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
