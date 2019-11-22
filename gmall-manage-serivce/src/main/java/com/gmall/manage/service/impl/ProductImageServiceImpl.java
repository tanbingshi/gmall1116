package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsProductImage;
import com.gmall.api.service.ProductImageService;
import com.gmall.manage.mapper.PmsProductImageMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        return pmsProductImageMapper.select(pmsProductImage);
    }
}
