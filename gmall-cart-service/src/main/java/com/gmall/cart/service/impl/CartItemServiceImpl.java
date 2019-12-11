package com.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.gmall.api.bean.OmsCartItem;
import com.gmall.api.service.CartItemService;
import com.gmall.cart.mapper.OmsCartItemMapper;
import com.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<OmsCartItem> getCartItemByMemberId(String memberId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        return omsCartItemMapper.select(omsCartItem);
    }

    @Override
    public void updateQuantityBy(String memberId,String skuId, BigDecimal quantity) {

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("productSkuId",skuId);

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setQuantity(quantity);

        omsCartItemMapper.updateByExampleSelective(omsCartItem,example);


        //同步缓存
        OmsCartItem omsCartItem1 = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItemList = omsCartItemMapper.select(omsCartItem1);
        Map<String,String> map = new HashMap<String,String>();
        for (OmsCartItem cartItem : omsCartItemList) {
            omsCartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getId(), JSON.toJSONString(cartItem));
        }

        Jedis jedis = redisUtil.getJedis();
        jedis.del("member:" + memberId +":cart");
        jedis.hmset("member:" + memberId +":cart",map);

        jedis.close();

    }

    @Override
    public void insert(OmsCartItem omsCartItem) {
        omsCartItemMapper.insert(omsCartItem);

        //同步缓存
        OmsCartItem omsCartItem1 = new OmsCartItem();
        omsCartItem.setMemberId(omsCartItem.getMemberId());
        List<OmsCartItem> omsCartItemList = omsCartItemMapper.select(omsCartItem1);
        Map<String,String> map = new HashMap<String,String>();
        for (OmsCartItem cartItem : omsCartItemList) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getId(), JSON.toJSONString(cartItem));
        }

        Jedis jedis = redisUtil.getJedis();
        jedis.del("member:" + omsCartItem.getMemberId() +":cart");
        jedis.hmset("member:" + omsCartItem.getMemberId() +":cart",map);

        jedis.close();
    }

    @Override
    public List<OmsCartItem> getCartItem(String memberId) {
        List<OmsCartItem> omsCartItemList = new ArrayList<>();

        Jedis jedis = redisUtil.getJedis();
        //考虑到缓存击穿问题
        Map<String, String> map = jedis.hgetAll("member:" + memberId + ":cart");
        if(map != null){
            //缓存有数据
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String value = entry.getValue();
                OmsCartItem omsCartItem = JSON.parseObject(value, OmsCartItem.class);
                omsCartItemList.add(omsCartItem);
            }
        }else{
            //缓存无数据
            omsCartItemList = getCartItemByMemberId(memberId);
            if(omsCartItemList == null){
                //设置缓存为null
                //jedis.del("member:" + memberId + ":cart");
                jedis.hmset("member:" + memberId + ":cart",null);
            }else{
                //同步缓存
                map = new HashMap<>();
                for (OmsCartItem omsCartItem : omsCartItemList) {
                    omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
                    map.put(omsCartItem.getId(),JSON.toJSONString(omsCartItem));
                }
                jedis.del("member:" + memberId + ":cart");
                jedis.hmset("member:" + memberId + ":cart",map);
            }
        }

        jedis.close();
        return omsCartItemList;
    }

    @Override
    public List<OmsCartItem> checkCart(String memberId, String skuId, String isChecked) {

        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId",memberId).andEqualTo("productSkuId",skuId);

        OmsCartItem omsCartItem = new OmsCartItem();omsCartItem.setIsChecked(isChecked);

        omsCartItemMapper.updateByExampleSelective(omsCartItem, example);

        List<OmsCartItem> omsCartItemList = omsCartItemMapper.selectAll();
        Jedis jedis = redisUtil.getJedis();
        //同步缓存
        Map<String,String> map = new HashMap<>();
        for (OmsCartItem cartItem : omsCartItemList) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
            map.put(cartItem.getId(),JSON.toJSONString(cartItem));
        }
        jedis.del("member:" + memberId + ":cart");
        jedis.hmset("member:" + memberId + ":cart",map);

        return omsCartItemList;
    }

    @Override
    public void delete(String memberId) {
        List<OmsCartItem> cartItem = getCartItemByMemberId(memberId);
        for (OmsCartItem omsCartItem : cartItem) {
            if(omsCartItem.getIsChecked().equals("1")){
                omsCartItemMapper.deleteByPrimaryKey(omsCartItem.getId());
            }
        }

    }
}
