package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.PmsBaseSaleAttr;
import com.gmall.api.bean.PmsProductImage;
import com.gmall.api.bean.PmsProductInfo;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.service.BaseSaleAttrService;
import com.gmall.api.service.ProductImageService;
import com.gmall.api.service.ProductInfoService;
import com.gmall.api.service.ProductSaleAttrService;
import com.gmall.manage.utils.PmsUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {

    @Reference
    ProductInfoService productInfoService;

    @Reference
    BaseSaleAttrService baseSaleAttrService;

    @Reference
    ProductSaleAttrService productSaleAttrService;

    @Reference
    ProductImageService productImageService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){
        return productInfoService.spuList(catalog3Id);
    }

    @ResponseBody
    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        return baseSaleAttrService.baseSaleAttrList();
    }

    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){

        String upload = PmsUploadUtil.upload(multipartFile);

        return upload;
    }

    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        productInfoService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @ResponseBody
    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        return productSaleAttrService.spuSaleAttrList(spuId);
    }

    @ResponseBody
    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){
        return productImageService.spuImageList(spuId);
    }

}
