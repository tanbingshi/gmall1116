package com.gmall.manage.mapper;

import com.gmall.api.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {

    List<PmsSkuInfo> selectHashTableBySkuId(String productId);
}
