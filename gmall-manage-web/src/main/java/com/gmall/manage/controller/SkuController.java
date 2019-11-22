package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.PmsSkuAttrValue;
import com.gmall.api.bean.PmsSkuInfo;
import com.gmall.api.bean.PmsSkuSaleAttrValue;
import com.gmall.api.service.SkuAttrValueService;
import com.gmall.api.service.SkuInfoService;
import com.gmall.api.service.SkuSaleAttrValueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class SkuController {

    @Reference
    SkuInfoService skuInfoService;

    @ResponseBody
    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        skuInfoService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }

}
