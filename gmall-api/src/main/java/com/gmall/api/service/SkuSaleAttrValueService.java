package com.gmall.api.service;

import com.gmall.api.bean.PmsSkuSaleAttrValue;

import java.util.List;

public interface SkuSaleAttrValueService {
    List<PmsSkuSaleAttrValue> spuSaleAttrList(String spuId);
}
