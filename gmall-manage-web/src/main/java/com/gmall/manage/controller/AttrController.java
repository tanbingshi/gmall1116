package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.PmsBaseAttrInfo;
import com.gmall.api.bean.PmsBaseAttrValue;
import com.gmall.api.service.BaseAttrInfoService;
import com.gmall.api.service.BaseAttrValueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    BaseAttrInfoService baseAttrInfoService;

    @Reference
    BaseAttrValueService baseAttrValueService;

    @ResponseBody
    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){
        return baseAttrInfoService.attrInfoList(catalog3Id);
    }

    @ResponseBody
    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        try {
            baseAttrInfoService.saveAttrInfo(pmsBaseAttrInfo);
        }catch(Exception e){
            throw new RuntimeException();
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        return baseAttrValueService.getAttrValueList(attrId);
    }

}
