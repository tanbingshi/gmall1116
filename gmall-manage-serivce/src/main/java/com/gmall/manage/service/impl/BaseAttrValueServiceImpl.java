package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsBaseAttrValue;
import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.service.BaseAttrValueService;
import com.gmall.manage.mapper.PmsBaseAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BaseAttrValueServiceImpl implements BaseAttrValueService {

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }

}
