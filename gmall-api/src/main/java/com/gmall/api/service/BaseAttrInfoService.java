package com.gmall.api.service;

import com.gmall.api.bean.PmsBaseAttrInfo;

import java.util.List;

public interface BaseAttrInfoService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
}
