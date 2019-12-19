package org.template.demo.query;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    private static final String ORDERS = "orders";

    @Bean
    Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(ORDERS).build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(ORDERS).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding());
    }
}
