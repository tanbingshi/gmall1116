package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsProductImage;
import com.gmall.api.bean.PmsProductInfo;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.bean.PmsProductSaleAttrValue;
import com.gmall.api.service.ProductInfoService;
import com.gmall.manage.mapper.PmsProductImageMapper;
import com.gmall.manage.mapper.PmsProductInfoMapper;
import com.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        //save PmsProductInfo
        PmsProductInfo pmsProductInfo1 = new PmsProductInfo();
        pmsProductInfo1.setDescription(pmsProductInfo.getDescription());
        pmsProductInfo1.setCatalog3Id(pmsProductInfo.getCatalog3Id());
        pmsProductInfo1.setProductName(pmsProductInfo.getProductName());
        pmsProductInfoMapper.insert(pmsProductInfo1);

        //save PmsProductImage
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pmsProductInfo1.getId());
            pmsProductImageMapper.insert(pmsProductImage);
        }

        //save PmsProductSaleAttr PmsProductSaleAttValue
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(pmsProductInfo1.getId());
            //save PmsProductSaleAttr
            pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);

            //save PmsProductSaleAttrValue
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(pmsProductInfo1.getId());
                pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
            }

        }


    }
}
