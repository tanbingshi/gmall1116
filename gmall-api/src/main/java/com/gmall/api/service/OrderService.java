package com.gmall.api.service;

import com.gmall.api.bean.OmsOrder;

public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId, String tradeCode);

    void save(OmsOrder omsOrder);

    OmsOrder getOrderInfoByOrderSn(String outOrderNo);

    void updateStatus(String out_trade_no);
}
