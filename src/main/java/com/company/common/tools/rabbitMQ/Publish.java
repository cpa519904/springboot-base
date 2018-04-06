package com.company.common.tools.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调。
 * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
 */
//@Component
//public class Publish{
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    public void delayQueue(String queueName, String msg){
//        rabbitTemplate.convertAndSend(queueName, msg);
//    }
//}
