package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.gmall.api.bean.*;
import com.gmall.api.service.SkuInfoService;
import com.gmall.manage.mapper.*;
import com.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class SkuInfoServiceImpl implements SkuInfoService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        //save PmsSKuInfo
        pmsSkuInfoMapper.insert(pmsSkuInfo);
        String skuInfoId = pmsSkuInfo.getId();

        //save PmsSkuImage
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuInfoId);
            pmsSkuImageMapper.insert(pmsSkuImage);
        }

        //save PmsSkuAttrValue
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuInfoId);
            pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
        }

        //save PmsSkuSaleAttrValue
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuInfoId);
            pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
        }

    }

    public PmsSkuInfo getSkuInfoByIdFromDB(String skuId){
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);

        pmsSkuInfo1.setSkuImageList(pmsSkuImages);

        return pmsSkuInfo1;
    }

    @Override
    public PmsSkuInfo getSkuInfoById(String skuId) {
        //如果redis 宕机,采取分布式锁
        Jedis jedis = redisUtil.getJedis();
        //判断redis是否存在
        String key = "sku:" + skuId + ":info";
        String value = jedis.get(key);
        if (value != null) {
            //存在,取出并返回
            PmsSkuInfo pmsSkuInfo = JSON.parseObject(value, PmsSkuInfo.class);
            return pmsSkuInfo;
        } else {
            //不存在,从数据库中取出,并存入redis中,并且使用redis set,nx,px自带的分布式锁,锁住第一个请求
            String keyLock = "sku:" + skuId + ":lock";
            //从数据库中取出数据
            PmsSkuInfo skuInfoByIdFromDB = null;
            String token = UUID.randomUUID().toString();
            String ok = jedis.set(keyLock, token, "NX", "PX", 10 * 1000);
            if (ok != null && ok.equals("OK")) {
                skuInfoByIdFromDB = getSkuInfoByIdFromDB(skuId);
                String skuInfoJSON = JSON.toJSONString(skuInfoByIdFromDB);
                //存入redis中
                jedis.set(key, skuInfoJSON);

                //删除skuXXXlock
                String lockToken = jedis.get(keyLock);
                if(lockToken != null && lockToken.equals(token)){
                    jedis.del(keyLock);
                }
            }
            jedis.close();
            return skuInfoByIdFromDB;
        }
    }
//以下代码是在redis没有宕机情况下
//       判断redis是否存在
//         String key = "sku:" + skuId + ":info";
//        Jedis jedis = redisUtil.getJedis();
//        String value = jedis.get(key);
//        if(value == null){
//            //如果是空,则查询数据库,并存入redis返回结果
//            PmsSkuInfo skuInfoByIdFromDB = getSkuInfoByIdFromDB(skuId);
//            String skuInfoByIdFromDBJSON = JSON.toJSONString(skuInfoByIdFromDB);
//            jedis.set(key,skuInfoByIdFromDBJSON);
//            return skuInfoByIdFromDB;
//        }else{
//            //如果不为空,则取出转化Object
//            PmsSkuInfo pmsSkuInfo = JSON.parseObject(value, PmsSkuInfo.class);
//            return pmsSkuInfo;
//        }


    @Override
    public List<PmsProductSaleAttr> getAttrValue(String skuId, String productId) {
        List<PmsProductSaleAttr>  pmsProductSaleAttrs=pmsSkuSaleAttrValueMapper.selectAttrValue(skuId, productId);
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsSkuInfo> getHashTableBySkuId(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectHashTableBySkuId(productId);
        return pmsSkuInfos;
    }


    @Override
    public List<PmsSkuInfo> getAll() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String id = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(id);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(select);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {

        boolean flag = false;
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectByPrimaryKey(productSkuId);
        if(skuInfo.getPrice().compareTo(price) == 0 ){
            flag =true;
        }

        return flag;
    }
}
