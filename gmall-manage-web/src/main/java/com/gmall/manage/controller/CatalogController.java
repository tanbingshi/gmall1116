package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.PmsBaseCatalog1;
import com.gmall.api.bean.PmsBaseCatalog2;
import com.gmall.api.bean.PmsBaseCatalog3;
import com.gmall.api.service.BaseCatalog1Service;
import com.gmall.api.service.BaseCatalog2Service;
import com.gmall.api.service.BaseCatalog3Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class CatalogController {

    @Reference
    BaseCatalog1Service baseCatalog1Service;

    @Reference
    BaseCatalog2Service baseCatalog2Service;

    @Reference
    BaseCatalog3Service baseCatalog3Service;

    @ResponseBody
    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        return baseCatalog1Service.getCatalog1();
    }

    @ResponseBody
    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        return baseCatalog2Service.getCatalog2(catalog1Id);
    }

    @ResponseBody
    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        return baseCatalog3Service.getCatalog3(catalog2Id);
    }

}
