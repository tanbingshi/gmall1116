package com.gmall.api.service;

import com.gmall.api.bean.OmsCartItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartItemService {

    List<OmsCartItem> getCartItemByMemberId(String memberId);

    void updateQuantityBy(String memberId,String skuId, BigDecimal quantity);

    void insert(OmsCartItem omsCartItem);

    List<OmsCartItem> getCartItem(String memberId);

    List<OmsCartItem> checkCart(String memberId, String skuId, String isChecked);

    void delete(String memberId);

}
