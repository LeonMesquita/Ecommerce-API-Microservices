package com.leonmesquita.ecommerce.cart_microservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.create-cart}")
    private String createCartQueue;

    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;

    @Bean
    public Queue createCartQueue() {
        return new Queue(createCartQueue, true);
    }

    @Bean
    public Queue clearCartQueue() {
        return new Queue(clearCartQueue, true);
    }
}