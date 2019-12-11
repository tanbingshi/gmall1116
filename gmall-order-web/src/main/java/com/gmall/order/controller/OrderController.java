package com.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.annotation.LoginAnnotation;
import com.gmall.api.bean.OmsCartItem;
import com.gmall.api.bean.OmsOrder;
import com.gmall.api.bean.OmsOrderItem;
import com.gmall.api.bean.UmsMemberReceiveAddress;
import com.gmall.api.service.CartItemService;
import com.gmall.api.service.MemberReceiveAddressService;
import com.gmall.api.service.OrderService;
import com.gmall.api.service.SkuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Reference
    CartItemService cartItemService;

    @Reference
    MemberReceiveAddressService memberReceiveAddressService;

    @Reference
    OrderService orderService;

    @Reference
    SkuInfoService skuInfoService;

    @RequestMapping("toTrade")
    @LoginAnnotation(loginSuccess = true)
    public String toTrade(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            ModelMap modelMap
            ){

        String memberId = "1";
        BigDecimal totalAmount = new BigDecimal("0");
        //查询购物车
        List<OmsCartItem> omsCartItems = cartItemService.getCartItemByMemberId(memberId);
        //把勾选的状态添加到订单中
        List<OmsOrderItem> orderItems = new ArrayList<>();
        for (OmsCartItem omsCartItem : omsCartItems) {
            if(omsCartItem.getIsChecked().equals("1")){
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                //productId
                omsOrderItem.setProductId(omsCartItem.getProductId());
                //productName
                omsOrderItem.setProductName(omsCartItem.getProductName());
                //productPic
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                //price
                omsOrderItem.setProductPrice(omsCartItem.getPrice()+"");
                //quantity
                omsOrderItem.setProductQuantity(Integer.parseInt(omsCartItem.getQuantity()+""));
                //skuId
                omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());

                totalAmount = totalAmount.add(omsCartItem.getPrice().add(omsCartItem.getQuantity()));
                orderItems.add(omsOrderItem);
            }
        }

        modelMap.put("orderDetailList",orderItems);
        modelMap.put("totalAmount",totalAmount);

        //查询收货地址
        List<UmsMemberReceiveAddress> address = memberReceiveAddressService.getMemberReceiveAddressByMemberId(memberId);
        modelMap.put("userAddressList",address);

        //生成tradeCode
       String tradeCode = orderService.genTradeCode(memberId);
       modelMap.put("tradeCode", tradeCode);

        return "trade";
    }

    @RequestMapping("submitOrder")
    @LoginAnnotation(loginSuccess = true)
    public ModelAndView submitOrder(
            String addressId,
            String totalAmount,
            String tradeCode,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ){
        String memberId = "1";

        ModelAndView modelAndView = new ModelAndView();

        //检查tradeCode

        String flag = orderService.checkTradeCode(memberId, tradeCode);
        if(flag.equals("success")){

            //订单对象
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setCreateTime(new Date());
            omsOrder.setMemberId(memberId);

            //根据addressId 查询收货地址
            UmsMemberReceiveAddress receiveAddress = memberReceiveAddressService.getMemberReceiveAddress(addressId);
            omsOrder.setReceiverName(receiveAddress.getName());
            omsOrder.setReceiverCity(receiveAddress.getCity());
            omsOrder.setReceiverProvince(receiveAddress.getProvince());
            omsOrder.setReceiverRegion(receiveAddress.getRegion());
            omsOrder.setReceiverDetailAddress(receiveAddress.getDetailAddress());

            String orderNs = "gmall";
            orderNs = orderNs + System.currentTimeMillis();
            omsOrder.setOrderSn(orderNs);
            omsOrder.setPayAmount(new BigDecimal(totalAmount));

            //订单详情
            List<OmsOrderItem> orderItemList = new ArrayList<>();

            //根据memberId查询购物清单
            List<OmsCartItem> cartItems = cartItemService.getCartItemByMemberId(memberId);
            for (OmsCartItem omsCartItem : cartItems) {
                if(omsCartItem.getIsChecked().equals("1")){
                    //封装订单详情对象
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    //productId
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    //productName
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    //productPic
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    //price
                    omsOrderItem.setProductPrice(omsCartItem.getPrice()+"");
                    //quantity
                    omsOrderItem.setProductQuantity(Integer.parseInt(omsCartItem.getQuantity()+""));
                    //skuId
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsCartItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsCartItem.setProductSn(orderNs);
                    //校验价格
                   boolean result = skuInfoService.checkPrice(omsCartItem.getProductSkuId(),omsCartItem.getPrice());
                   if(result == false){
                       modelAndView.setViewName("tradeFail");
                       return modelAndView;
                   }

                    orderItemList.add(omsOrderItem);
                }
            }

            //把orderItem封装order
            omsOrder.setOmsOrderItemList(orderItemList);
            //保存order
            orderService.save(omsOrder);

            //删除购物车
            cartItemService.delete(memberId);

            //重定向到支付页面
            modelAndView.setViewName("redirect:http://127.0.0.1:8087/index");
            modelAndView.addObject("outOrderNo",orderNs);
            modelAndView.addObject("totalAmount",totalAmount);


            return modelAndView;

        }else{
            modelAndView.setViewName("tradeFail");
            return modelAndView;
        }

    }





}
