package com.rabbitmq.demo.Controller;

import com.rabbitmq.demo.model.Employee;
import com.rabbitmq.demo.service.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitController {
    @Autowired
    RabbitMQSender rabbitMQSender;

    @GetMapping("/rabbit")
    public ResponseEntity<?> sendMsg() {
        Employee employee = new Employee();
        employee.setEmpId("1");
        employee.setEmpName("duy");
        rabbitMQSender.send(employee);
        rabbitMQSender.sendToFanoutExchange(employee);
        rabbitMQSender.sendToTopicExchangeError(employee);
        rabbitMQSender.sendToTopicExchangeWarn(employee);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/dlq")
    public ResponseEntity<?> sendMsgDLQ() {
        Employee employee = new Employee();
        employee.setEmpId("1");
        employee.setEmpName("duy");
        rabbitMQSender.sendToDeadLetterQueue(employee);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
