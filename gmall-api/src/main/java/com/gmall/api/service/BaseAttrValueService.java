package com.gmall.api.service;

import com.gmall.api.bean.PmsBaseAttrValue;
import com.gmall.api.bean.PmsProductSaleAttr;

import java.util.List;

public interface BaseAttrValueService {
    List<PmsBaseAttrValue> getAttrValueList(String attrId);

}
