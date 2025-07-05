package com.leonmesquita.ecommerce.product_microservice.configs;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.update-stock}")
    private String updateStockQueue;

    @Value("${mq.queues.update-stock-dlq}")
    private String updateStockDLQ;

    @Bean
    public Queue updateStockQueue() {
        return QueueBuilder.durable(updateStockQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", updateStockDLQ)
                .withArgument("x-max-length", 1000) // evita fila crescer infinitamente
                .withArgument("x-message-ttl", 1000) // 1 segundo entre tentativas
                .build();
    }

    @Bean
    public Queue updateStockDlqQueue() {
        return QueueBuilder.durable(updateStockDLQ).build();
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false); // NÃO reencaminha a mesma mensagem de volta infinitamente
        return factory;
    }
}