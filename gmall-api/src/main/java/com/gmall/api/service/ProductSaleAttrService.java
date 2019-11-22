package com.gmall.api.service;

import com.gmall.api.bean.PmsProductSaleAttr;

import java.util.List;

public interface ProductSaleAttrService {
    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);
}
