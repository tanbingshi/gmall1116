package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsSkuAttrValue;
import com.gmall.api.bean.PmsSkuImage;
import com.gmall.api.bean.PmsSkuInfo;
import com.gmall.api.bean.PmsSkuSaleAttrValue;
import com.gmall.api.service.SkuInfoService;
import com.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.gmall.manage.mapper.PmsSkuImageMapper;
import com.gmall.manage.mapper.PmsSkuInfoMapper;
import com.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
}
