package com.gmall.api.service;

import com.gmall.api.bean.PaymentInfo;

public interface PaymentService {
    void savePayment(PaymentInfo paymentInfo);

    void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfo);
}
