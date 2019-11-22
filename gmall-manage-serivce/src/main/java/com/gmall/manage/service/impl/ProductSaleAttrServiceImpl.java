package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.bean.PmsProductSaleAttrValue;
import com.gmall.api.service.ProductSaleAttrService;
import com.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductSaleAttrServiceImpl implements ProductSaleAttrService {

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrList) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
        }

        return pmsProductSaleAttrList;
    }
}
