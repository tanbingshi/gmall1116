package com.gmall.order.mq;

import com.gmall.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;

@Component
public class OrderServiceMq {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_QUEUE", containerFactory = "jmsQueueListener")
    public void updateOrderStatus(MapMessage mapMessage) throws JMSException {
        String out_trade_no = mapMessage.getString("out_trade_no");
        if(out_trade_no != null){
            //执行更新订单状态
            orderService.updateStatus(out_trade_no);
        }
    }

}
