package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsSkuSaleAttrValue;
import com.gmall.api.service.SkuSaleAttrValueService;
import com.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SkuSaleAttrValueServiceImpl implements SkuSaleAttrValueService {

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Override
    public List<PmsSkuSaleAttrValue> spuSaleAttrList(String spuId) {
        return null;
    }
}
