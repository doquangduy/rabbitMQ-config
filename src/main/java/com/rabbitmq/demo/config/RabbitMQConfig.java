package com.rabbitmq.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue}")
    String queueName;

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    @Value("${rabbitmq.topic.queue1}")
    private String topicQueue1;

    @Value("${rabbitmq.topic.queue2}")
    private String topicQueue2;

    @Value("${rabbitmq.fanout.queue1}")
    private String fanoutQueue1;

    @Value("${rabbitmq.fanout.queue2}")
    private String fanoutQueue2;

    @Value("${rabbitmq.fanout.exchange}")
    private String fanoutExchange;

    @Value("${rabbitmq.topic.exchange}")
    private String topicExchange;
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
   public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange);
   }
    @Bean
    public Declarables fanoutBindings(FanoutExchange fanoutExchange) {
        Queue fanoutQueue1 = new Queue(this.fanoutQueue1, false);
        Queue fanoutQueue2 = new Queue(this.fanoutQueue2, false);

        return new Declarables(
          fanoutQueue1,
          fanoutQueue2,
          fanoutExchange,
          BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
          BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
    }
    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }
    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable("deadLetterQueue").build();
    }
    @Bean
    DirectExchange exchangeDirect() {
        return new DirectExchange("javainuse-direct-exchange");
    }
    @Bean
    Queue queue2() {
        return QueueBuilder.durable("javainuse.queue").withArgument("x-dead-letter-exchange", "deadLetterExchange")
          .withArgument("x-dead-letter-routing-key", "deadLetter").build();
    }

    @Bean
    Binding bindingDeadLetter() {
        return BindingBuilder.bind(queue2()).to(exchangeDirect()).with("javainuse");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    public Declarables topicBindings(TopicExchange topicExchange) {
        Queue topicQueue1 = new Queue(this.topicQueue1, false);
        Queue topicQueue2 = new Queue(this.topicQueue2, false);
        return new Declarables(
          topicQueue1,
          topicQueue2,
          topicExchange,
          BindingBuilder
            .bind(topicQueue1)
            .to(topicExchange).with("*.important.*"),
          BindingBuilder
            .bind(topicQueue2)
            .to(topicExchange).with("#.error"));
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


//    @Bean
//    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
}