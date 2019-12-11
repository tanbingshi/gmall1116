package com.gmall.api.service;

import com.gmall.api.bean.PmsProductInfo;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.bean.PmsProductSaleAttrValue;
import com.gmall.api.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

public interface SkuInfoService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuInfoById(String skuId);

    List<PmsProductSaleAttr> getAttrValue(String skuId, String productId);

    List<PmsSkuInfo> getHashTableBySkuId(String productId);

    List<PmsSkuInfo> getAll();

    boolean checkPrice(String productSkuId, BigDecimal price);
}
