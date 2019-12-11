package com.gmall.manage.mapper;

import com.gmall.api.bean.PmsProductSaleAttr;
import com.gmall.api.bean.PmsProductSaleAttrValue;
import com.gmall.api.bean.PmsSkuSaleAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuSaleAttrValueMapper extends Mapper<PmsSkuSaleAttrValue> {

    List<PmsProductSaleAttr> selectAttrValue(@Param("skuId") String skuId, @Param("productId") String productId);
}
