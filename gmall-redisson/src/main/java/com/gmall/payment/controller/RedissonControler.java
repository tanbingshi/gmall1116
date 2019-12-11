package com.gmall.payment.controller;

import com.gmall.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
public class RedissonControler {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @ResponseBody
    @RequestMapping("testRedisson")
    public String testRedisson(){
        Jedis jedis = redisUtil.getJedis();
        RLock anyLock = redissonClient.getLock("anyLock");
        anyLock.lock();
        //进行redisson压力测试
        try{
        String key = "k";
        String s = jedis.get(key);
        if(s == null || s.equals("")){
            jedis.set(key,"1");
        }else{
            jedis.set(key,(Integer.parseInt(s)+1)+"");
        }
        System.out.println(s);
        }finally {
            jedis.close();
            anyLock.unlock();
        }

        return "success";
    }

}
