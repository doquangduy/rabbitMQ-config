package com.rabbitmq.demo.service;

import com.rabbitmq.demo.model.Employee;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    @Value("${rabbitmq.fanout.exchange}")
    private String fanoutExchange;

    @Value("${rabbitmq.topic.exchange}")
    String topicExchange;

    private static final String TOPIC_ROUTING_KEY_IMPORTANT_WARN="demo.important.warn";

    private static final String TOPIC_ROUTING_KEY_ERROR="demo.error";
    public void send(Employee company) {
        rabbitTemplate.convertAndSend(exchange, routingkey, company);
        System.out.println("Send msg = " + company);

    }
    public void sendToFanoutExchange(Employee employee) {
        rabbitTemplate.convertAndSend(fanoutExchange,"", employee);
        System.out.println("Send msg = " + employee);
    }

    public void sendToTopicExchangeWarn(Employee employee) {
        rabbitTemplate.convertAndSend(topicExchange, TOPIC_ROUTING_KEY_IMPORTANT_WARN,employee);
        System.out.println("Send msg = " + employee);
    }

    public void sendToTopicExchangeError(Employee employee) {
        rabbitTemplate.convertAndSend(topicExchange, TOPIC_ROUTING_KEY_ERROR ,employee);
        System.out.println("Send msg = " + employee);
    }

}