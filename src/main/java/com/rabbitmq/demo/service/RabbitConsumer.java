package com.rabbitmq.demo.service;

import com.rabbitmq.demo.model.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumer {

    @RabbitListener(queues = "${rabbitmq.fanout.queue1}")
    public void receiveMessageFromFanout1(Employee message) {
        System.out.println("Received fanout 1 message: " + message);
    }

    @RabbitListener(queues = "${rabbitmq.fanout.queue2}")
    public void receiveMessageFromFanout2(Employee message) {
        System.out.println("Received fanout 2 message: "+ message );
    }

    @RabbitListener(queues = "${rabbitmq.topic.queue1}")
    public void receiveMessageFromTopic1(Employee message) {
        System.out.println("Received topic 1 WARN message: " + message);
    }

    @RabbitListener(queues = "${rabbitmq.topic.queue2}")
    public void receiveMessageFromTopic2(Employee message) {
        System.out.println("Received topic 2 ERROR message: " + message);
    }
    @RabbitListener(queues = "javainuse.queue")
    public void receiveMessageDLQ(String message) throws Exception {
//        throw new Exception("e");
        System.out.println("Received topic 2 ERROR message: " + message);
    }
}
