package com.gmall.payment.service.impl;

import com.gmall.api.bean.PaymentInfo;
import com.gmall.api.service.PaymentService;
import com.gmall.mq.ActiveMQUtil;
import com.gmall.payment.mapper.PaymentMapper;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public void savePayment(PaymentInfo paymentInfo) {
        paymentMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfo) {

        Example example = new Example(PaymentInfo.class);
        example.createCriteria().andEqualTo("orderSn",out_trade_no);

        ConnectionFactory connectionFactory = activeMQUtil.getConnectionFactory();
        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);

            Queue payment_queue = session.createQueue("PAYMENT_QUEUE");
            MessageProducer producer = session.createProducer(payment_queue);
            ActiveMQMapMessage activeMQMapMessage = new ActiveMQMapMessage();
            activeMQMapMessage.setString("out_trade_no",out_trade_no);

            paymentMapper.updateByExampleSelective(paymentInfo,example);
            producer.send(activeMQMapMessage);

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if(session != null){
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if( connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
