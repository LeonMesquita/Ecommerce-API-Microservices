package com.leonmesquita.ecommerce.auth_microservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.create-cart}")
    private String createCartQueue;

    @Bean
    public Queue createCartQueue() {
        return new Queue(createCartQueue, true);
    }
}
