package com.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gmall.annotation.LoginAnnotation;
import com.gmall.api.bean.OmsCartItem;
import com.gmall.api.bean.PmsSkuInfo;
import com.gmall.api.service.CartItemService;
import com.gmall.api.service.SkuInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    CartItemService cartItemService;

    @Reference
    SkuInfoService skuInfoService;

    @RequestMapping("addToCart")
    public String addToCart(
            String skuId,
            int num,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
            ){

        //存放cartItem
        List<OmsCartItem> cartItemList = new ArrayList<>();

        //根据skuId查询该商品信息
        PmsSkuInfo pmsSkuInfo = skuInfoService.getSkuInfoById(skuId);

        //把上诉商品封装成OmsCartItem
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setQuantity(new BigDecimal(num));
        omsCartItem.setIsChecked("1");

        //memberId 来标志是否已经登录
        String memberId = "1";

        //获取目标Cookie
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if(cookies != null){
            for (Cookie temp : cookies) {
                String name = temp.getName();
                if( name != null){
                    if(name.equals("cartItemCookie")){
                        cookie = temp;
                        break;
                    }
                }

            }
        }

        if(StringUtils.isBlank(memberId)){
            //没有登录情况下,把购物车存入Cookie
            String cartItemJSON;
            //判断Cookie是否为空
            if(cookie == null){
                //存放cartItem
                cartItemList.add(omsCartItem);
                //cookie为空,新建cookie,并把购物车存入cookie
                cartItemJSON = JSON.toJSONString(cartItemList);
                try {
                    cartItemJSON = URLEncoder.encode(cartItemJSON, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cookie = new Cookie("cartItemCookie",cartItemJSON);
                response.addCookie(cookie);
            }else{
                //cookie不为空
                String value = cookie.getValue();
                try {
                    value = URLDecoder.decode(value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                List<OmsCartItem> cartItemListCookie = JSON.parseArray(value, OmsCartItem.class);

                //更新cartItem数量
                for (OmsCartItem cartItem : cartItemListCookie) {
                    if (cartItem.getProductSkuId().equals(skuId)){
                        cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                    }
                }
                cartItemJSON = JSON.toJSONString(cartItemListCookie);
                try {
                    cartItemJSON = URLEncoder.encode(cartItemJSON, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                cookie.setValue(cartItemJSON);
                response.addCookie(cookie);
            }

        }else{
            //如果已经登录
            //根据memberId查询该member的所有cartItem
            List<OmsCartItem> omsCartItemList = cartItemService.getCartItemByMemberId(memberId);
            omsCartItem.setMemberNickname("test");
            omsCartItem.setMemberId(memberId);
            //判断是否存在cartItem
            if(omsCartItemList != null && omsCartItemList.size() > 0){
                //存在cartItem
                //根据skuId判断,更新商品数量
                boolean flag = true;
                for (OmsCartItem cartItem : omsCartItemList) {
                    if(cartItem.getProductSkuId().equals(skuId)){
                        cartItem.setIsChecked(omsCartItem.getIsChecked());
                        cartItem.setQuantity(cartItem.getQuantity().add(new BigDecimal(num)));
                        cartItemService.updateQuantityBy(cartItem.getMemberId(),skuId,cartItem.getQuantity());
                        flag = false;
                        break;
                    }
                }
                if(flag){

                    //没有存在该商品的购物
                    //执行插入操作
                    cartItemService.insert(omsCartItem);
                }
            }
        }

        return "redirect:/success.html";
    }


    //查看购物车
    @RequestMapping("cartList")
    public String cartList(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            ModelMap modelMap
            ){

        //判断是否登录
        String memberId = "1";
        List<OmsCartItem> omsCartItemList = null;
        if(StringUtils.isBlank(memberId)){
            //没有登录,查询cookie
            Cookie[] cookies = request.getCookies();
            if( cookies != null){
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    //目标cookie
                    if(cookieName.equals("cartItemCookie")){
                        String cookieValue = cookie.getValue();
                        try {
                            cookieValue = URLDecoder.decode(cookieValue,"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        omsCartItemList = JSON.parseArray(cookieValue, OmsCartItem.class);
                        break;
                    }
                }
            }
            //计算总价格
            if( omsCartItemList != null){
                for (OmsCartItem omsCartItem : omsCartItemList) {
                    omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                }
            }

            //计算cartItem总价格
            BigDecimal totalAmount = getTotalAmount(omsCartItemList);
            modelMap.put("totalAmount", totalAmount);
            modelMap.put("cartList", omsCartItemList);
        }else{
            //登录,根据memberId查询缓存
           omsCartItemList = cartItemService.getCartItem(memberId);

            //计算总价格
            if( omsCartItemList != null){
                for (OmsCartItem omsCartItem : omsCartItemList) {
                    omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                }
            }
            //计算cartItem总价格
            BigDecimal totalAmount = getTotalAmount(omsCartItemList);
            modelMap.put("totalAmount", totalAmount);
            modelMap.put("cartList", omsCartItemList);
        }

        return "cartList";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItemList) {

        BigDecimal totalAmount = new BigDecimal("0");
        if(omsCartItemList != null){
            for (OmsCartItem omsCartItem : omsCartItemList) {
                if( omsCartItem.getIsChecked().equals("1")){
                    totalAmount = totalAmount.add(omsCartItem.getTotalPrice());
                }
            }
        }

        return totalAmount;
    }


    @RequestMapping("checkCart")
    public String checkCart(
            String skuId,
            String isChecked,
            ModelMap modelMap
    ){
        List<OmsCartItem> omsCartItemList;
        String memberId = "1";
        //根据memberId判断是否登录
        if(StringUtils.isBlank(memberId)){
            //没有登录

        }else{
            //登录
            omsCartItemList = cartItemService.checkCart(memberId,skuId,isChecked);
            //计算cartItem总价格
            BigDecimal totalAmount = getTotalAmount(omsCartItemList);
            modelMap.put("totalAmount", totalAmount);
            modelMap.put("cartList", omsCartItemList);
        }
        return "innerCart";
    }

    @RequestMapping("toTrade")
    @LoginAnnotation(loginSuccess = true)
    public String toTrade(){
        return "toTrade";
    }

}
