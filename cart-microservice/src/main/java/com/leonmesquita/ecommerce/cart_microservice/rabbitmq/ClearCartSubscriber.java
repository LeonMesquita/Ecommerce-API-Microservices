package com.leonmesquita.ecommerce.cart_microservice.rabbitmq;

import com.leonmesquita.ecommerce.cart_microservice.services.CartService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClearCartSubscriber {

    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;

    @Autowired
    CartService cartService;


    @RabbitListener(queues = "${mq.queues.clear-cart}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveClearCartRequest(Long cartId) {
        cartService.clearCart(cartId);
    }
}
