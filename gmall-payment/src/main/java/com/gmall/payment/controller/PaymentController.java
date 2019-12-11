package com.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.gmall.annotation.LoginAnnotation;
import com.gmall.api.bean.OmsOrder;
import com.gmall.api.bean.PaymentInfo;
import com.gmall.api.service.OrderService;
import com.gmall.api.service.PaymentService;
import com.gmall.payment.config.AlipayConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @Reference
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping("index")
    @LoginAnnotation(loginSuccess = true)
    public String index(String outOrderNo, String totalAmount, HttpServletRequest request, ModelMap modelMap){

        String memberId = "1";

        modelMap.put("outOrderNo",outOrderNo);
        modelMap.put("totalAmount",totalAmount);

        return "index";
    }

    @RequestMapping("alipay/submit")
    @ResponseBody
    @LoginAnnotation(loginSuccess = true)
    public String alipay(String outOrderNo, String totalAmount, HttpServletRequest request, ModelMap modelMap){

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);

        Map<String,Object> map = new HashMap<>();
        map.put("out_trade_no",outOrderNo);
        map.put("product_code","FAST_INSTANT_TRADE_PAY");
        map.put("total_amount",0.01);
        map.put("subject","谷粒购物test");

        String parse = JSON.toJSONString(map);
        alipayRequest.setBizContent(parse);

        String form=null;
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //生成并且保存用户支付信息
        //根据outOrderNo查询订单信息
        OmsOrder omsOrder = orderService.getOrderInfoByOrderSn(outOrderNo);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderId(omsOrder.getId());
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setPaymentStatus("未支付");
        paymentInfo.setOrderSn(outOrderNo);
        paymentInfo.setSubject("test");
        paymentInfo.setTotalAmount(new BigDecimal(totalAmount));
        //保存paymentInfo
        paymentService.savePayment(paymentInfo);

        return form;
    }


    @RequestMapping("wx/submit")
    @ResponseBody
    @LoginAnnotation(loginSuccess = true)
    public String mx(String outOrderNo, String totalAmount, HttpServletRequest request, ModelMap modelMap){

        return null;
    }

    @RequestMapping("alipay/callback/return")
    public String alipayCallbackReturn(HttpServletRequest request){


        // 回调请求中获取支付宝参数
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();


        // 通过支付宝的paramsMap进行签名验证，2.0版本的接口将paramsMap参数去掉了，导致同步请求没法验签
        if(StringUtils.isNotBlank(sign)){
            // 验签成功
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setAlipayTradeNo(trade_no);// 支付宝的交易凭证号
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());
            // 更新用户的支付状态
            paymentService.updatePaymentInfo(out_trade_no,paymentInfo);

        }


        return "finish";
    }

}
