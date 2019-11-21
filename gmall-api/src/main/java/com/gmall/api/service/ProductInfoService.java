package com.gmall.api.service;

import com.gmall.api.bean.PmsProductInfo;

import java.util.List;

public interface ProductInfoService {
    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);
}
