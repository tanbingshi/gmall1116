package com.gmall.api.service;

import com.gmall.api.bean.PmsSearchParam;
import com.gmall.api.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
