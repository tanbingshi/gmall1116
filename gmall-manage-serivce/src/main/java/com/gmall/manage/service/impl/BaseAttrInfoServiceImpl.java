package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsBaseAttrInfo;
import com.gmall.api.bean.PmsBaseAttrValue;
import com.gmall.api.service.BaseAttrInfoService;
import com.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.gmall.manage.mapper.PmsBaseAttrValueMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class BaseAttrInfoServiceImpl implements BaseAttrInfoService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfos;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        //根据ID删除PmsBaseAttrInfo
        if(pmsBaseAttrInfo.getId() != null){
            String id = pmsBaseAttrInfo.getId();
            pmsBaseAttrInfoMapper.deleteByPrimaryKey(id);
        }

        //重新插入PmsBaseAttrInfo
            PmsBaseAttrInfo pmsBaseAttrInfo1 = new PmsBaseAttrInfo();
        if( pmsBaseAttrInfo.getId() != null){
            pmsBaseAttrInfo1.setId(pmsBaseAttrInfo.getId());
        }
            pmsBaseAttrInfo1.setCatalog3Id(pmsBaseAttrInfo.getCatalog3Id());
            pmsBaseAttrInfo1.setAttrName(pmsBaseAttrInfo.getAttrName());
            pmsBaseAttrInfo1.setIsEnabled(pmsBaseAttrInfo.getIsEnabled());
            pmsBaseAttrInfoMapper.insert(pmsBaseAttrInfo1);

            //根据catalog3Id查询之前PmsBaseAttrInfo并删除
            String attrId = pmsBaseAttrInfo1.getId();
            Example example = new Example(PmsBaseAttrValue.class);
            example.createCriteria().andEqualTo("attrId",attrId);
            pmsBaseAttrValueMapper.deleteByExample(example);

            //重新插入PmsBaseAttrValue
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            if(attrValueList != null && attrValueList.size() > 0){
                pmsBaseAttrValueMapper.insertBatch(attrValueList,attrId);
            }
    }

    @Override
    public List<PmsBaseAttrInfo> getBaseAttrInfoWithBaseAttrValue(Set<String> valueIdSet) {

        String valueIdStr = StringUtils.join(valueIdSet, ",");
        return pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
    }


}
