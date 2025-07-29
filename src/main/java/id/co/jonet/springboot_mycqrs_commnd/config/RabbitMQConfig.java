package id.co.jonet.springboot_mycqrs_commnd.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${myapp.rabbitmq.exchange}")
    private String exchange;

    @Bean
    public DirectExchange personExchange() {
        return new DirectExchange(exchange);
    }

    // === Queue name and binding ===
    @Bean
    public Queue createPersonQueue() {
        return new Queue("person.create.queue", true); // true = durable
    }
    @Bean
    public Binding createPersonQueueBinding(Queue createPersonQueue, DirectExchange personExchange) {
        return BindingBuilder.bind(createPersonQueue).to(personExchange).with("CreatePersonEvent");
    }

    @Bean
    public Queue updatePersonQueue() {
        return new Queue("person.update.queue", true); // true = durable
    }
    @Bean
    public Binding updatePersonQueueBinding(Queue updatePersonQueue, DirectExchange personExchange) {
        return BindingBuilder.bind(updatePersonQueue).to(personExchange).with("UpdatePersonEvent");
    }

    @Bean
    public Queue deletePersonQueue() {
        return new Queue("person.delete.queue", true); // true = durable
    }
    @Bean
    public Binding deletePersonQueueBinding(Queue deletePersonQueue, DirectExchange personExchange) {
        return BindingBuilder.bind(deletePersonQueue).to(personExchange).with("DeletePersonEvent");
    }
    // === End of Queue name ===

    // === Converter ===
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}

