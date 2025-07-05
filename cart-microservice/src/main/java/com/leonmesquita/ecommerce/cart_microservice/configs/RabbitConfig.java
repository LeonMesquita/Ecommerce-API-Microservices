package com.leonmesquita.ecommerce.cart_microservice.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${mq.queues.create-cart}")
    private String createCartQueue;

    @Value("${mq.queues.clear-cart}")
    private String clearCartQueue;

    @Value("${mq.queues.create-cart-dlq}")
    private String createCartDLQ;

    @Value("${mq.queues.clear-cart-dlq}")
    private String clearCartDLQ;

    @Bean
    public Queue createCartQueue() {

        return QueueBuilder.durable(createCartQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", createCartDLQ)
                .withArgument("x-max-length", 1000) // evita fila crescer infinitamente
                .withArgument("x-message-ttl", 1000) // 1 segundo entre tentativas
                .build();
    }


    @Bean
    public Queue clearCartQueue() {
        return QueueBuilder.durable(clearCartQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", clearCartDLQ)
                .withArgument("x-max-length", 1000) // evita fila crescer infinitamente
                .withArgument("x-message-ttl", 1000) // 1 segundo entre tentativas
                .build();
    }

    @Bean
    public Queue createCartDLQQueue() {
        return QueueBuilder.durable(createCartDLQ).build();
    }

    @Bean
    public Queue clearCartDLQQueue() {
        return QueueBuilder.durable(clearCartDLQ).build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false); // NÃO reencaminha a mesma mensagem de volta infinitamente
        return factory;
    }
}