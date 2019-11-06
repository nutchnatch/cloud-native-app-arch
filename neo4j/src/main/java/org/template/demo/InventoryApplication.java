package org.template.demo;

import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

 public static void main(String[] args) {
  SpringApplication.run(InventoryApplication.class, args);
 }

 @Bean
 SpringAMQPMessageSource shipments(Serializer serializer) {
  return new SpringAMQPMessageSource(serializer) {

   @Override
   @RabbitListener(queues = "orders")
   public void onMessage(Message message, Channel channel) throws Exception {
    super.onMessage(message, channel);
   }
  };
 }
}