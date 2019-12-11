package com.gmall.manage.mapper;

import com.gmall.api.bean.PmsProductInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductInfoMapper extends Mapper<PmsProductInfo> {

    List<PmsProductInfo> selectHashTableBySkuId(@Param("productId") String productId);
}
