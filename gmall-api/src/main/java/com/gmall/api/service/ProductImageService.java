package com.gmall.api.service;

import com.gmall.api.bean.PmsProductImage;

import java.util.List;

public interface ProductImageService {
    List<PmsProductImage> spuImageList(String spuId);
}
