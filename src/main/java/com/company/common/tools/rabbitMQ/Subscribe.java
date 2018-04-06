package com.company.common.tools.rabbitMQ;

import com.company.common.tools.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
//public class Subscribe {
//    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
//
//    @RabbitListener(queues = Constants.ORDER_DEAD_LETTER_QUEUE_NAME)
//    public void process(String content){
//        logger.info(String.format("订单超过30分钟未支付，订单号:%s", content));
//    }
//}
