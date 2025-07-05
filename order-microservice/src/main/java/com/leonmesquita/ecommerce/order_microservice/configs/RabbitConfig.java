package com.leonmesquita.ecommerce.order_microservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;

    @Value("${mq.queues.update-stock}")
    private String updateStockQueue;

    @Bean
    public Queue clearCartQueue() {
        return new Queue(clearCartQueue, true);
    }
    @Bean
    public Queue updateStockQueue() {
        return new Queue(updateStockQueue, true);
    }

}