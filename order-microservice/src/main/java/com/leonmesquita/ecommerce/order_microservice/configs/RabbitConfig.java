package com.leonmesquita.ecommerce.order_microservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;

    @Value("${mq.queues.update-status}")
    private String updateStatus;

    @Value("${mq.queues.update-status-dlq}")
    private String updateStatusDLQ;


    @Bean
    public Queue updateStatusQueue() {
        return QueueBuilder.durable(updateStatus)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", updateStatusDLQ)
                .withArgument("x-max-length", 1000) // evita fila crescer infinitamente
                .withArgument("x-message-ttl", 1000) // 1 segundo entre tentativas
                .build();
    }

}