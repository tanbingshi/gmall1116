package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsBaseSaleAttr;
import com.gmall.api.service.BaseSaleAttrService;
import com.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BaseSaleAttrServiceImpl implements BaseSaleAttrService {

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }
}
