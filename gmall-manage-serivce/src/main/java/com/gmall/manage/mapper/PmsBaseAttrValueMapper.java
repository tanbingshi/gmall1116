package com.gmall.manage.mapper;

import com.gmall.api.bean.PmsBaseAttrValue;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrValueMapper extends Mapper<PmsBaseAttrValue> {
    void insertBatch(@Param("attrValueList") List<PmsBaseAttrValue> attrValueList,@Param("attrId") String attrId);
}
