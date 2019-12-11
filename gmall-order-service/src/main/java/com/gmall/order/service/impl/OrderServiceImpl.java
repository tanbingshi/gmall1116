package com.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.OmsOrder;
import com.gmall.api.bean.OmsOrderItem;
import com.gmall.api.service.OrderService;
import com.gmall.mq.ActiveMQUtil;
import com.gmall.order.mapper.OmsOrderItemMapper;
import com.gmall.order.mapper.OmsOrderMapper;
import com.gmall.util.RedisUtil;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OmsOrderMapper omsOrderMapper;

    @Autowired
    OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Override
    public String genTradeCode(String memberId) {
        Jedis jedis = redisUtil.getJedis();
        String key = "member:" + memberId + ":tradeCode";
        String value = UUID.randomUUID().toString();
        jedis.setex(key,60 * 15 * 1000, value);

        jedis.close();
        return value;
    }

    @Override
    public String checkTradeCode(String memberId, String tradeCode) {

        Jedis jedis = null;
        try{
            jedis = redisUtil.getJedis();
            String key = "member:" + memberId + ":tradeCode";
            String value = jedis.get(key);
            if(StringUtils.isNotBlank(value) && value.equals(tradeCode)){
                jedis.del(key);
                return "success";
            }else{
                return "fail";
            }

        }catch (Exception e){

        }finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public void save(OmsOrder omsOrder) {
         omsOrderMapper.insert(omsOrder);

        List<OmsOrderItem> omsOrderItemList = omsOrder.getOmsOrderItemList();
        for (OmsOrderItem omsOrderItem : omsOrderItemList) {
            omsOrderItem.setOrderId(omsOrder.getId());
            omsOrderItemMapper.insert(omsOrderItem);
        }
    }

    @Override
    public OmsOrder getOrderInfoByOrderSn(String outOrderNo) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setOrderSn(outOrderNo);

        return omsOrderMapper.selectOne(omsOrder);
    }

    @Override
    public void updateStatus(String out_trade_no) {

        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setStatus("1");

        Example example = new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("orderSn",out_trade_no);

        ConnectionFactory connectionFactory = activeMQUtil.getConnectionFactory();
        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);

            Queue payment_queue = session.createQueue("ORDER_QUEUE");
            MessageProducer producer = session.createProducer(payment_queue);
            ActiveMQMapMessage activeMQMapMessage = new ActiveMQMapMessage();
            //activeMQMapMessage.setString("out_trade_no",out_trade_no);

            omsOrderMapper.updateByExampleSelective(omsOrder,example);
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
