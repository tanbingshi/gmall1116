package com.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gmall.api.bean.PmsProductInfo;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.bean.PmsSkuInfo;
import com.gmall.api.bean.PmsSkuSaleAttrValue;
import com.gmall.api.service.SkuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuInfoService skuInfoService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable("skuId") String skuId, ModelMap map){
        //根据skuId查询PmsSkuInfo
        PmsSkuInfo pmsSkuInfo = skuInfoService.getSkuInfoById(skuId);
        map.put("skuInfo", pmsSkuInfo);

        //查询属性值
        List<PmsProductSaleAttr> attrValues = skuInfoService.getAttrValue(skuId,pmsSkuInfo.getProductId());
        map.put("spuSaleAttrListCheckBySku",attrValues);

        //hash 根据productId 查询skuId商品的sibling
        List<PmsSkuInfo> hashSkuInfos = skuInfoService.getHashTableBySkuId(pmsSkuInfo.getProductId());
        Map<String,String> mapStr = new HashMap<String,String>();

        for (PmsSkuInfo hashSkuInfo : hashSkuInfos) {
            String key = "";
            String value = "";
            value = hashSkuInfo.getId();

            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = hashSkuInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                key += pmsSkuSaleAttrValue.getId() + "|";
            }
            mapStr.put(key, value);
        }

        String jsonString = JSON.toJSONString(mapStr);
        map.put("hashTableValue", jsonString);

        return "item";
    }


}
