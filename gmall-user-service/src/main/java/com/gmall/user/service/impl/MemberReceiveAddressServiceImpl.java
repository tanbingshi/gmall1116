package com.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.UmsMemberReceiveAddress;
import com.gmall.api.service.MemberReceiveAddressService;
import com.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberReceiveAddressServiceImpl implements MemberReceiveAddressService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public int addMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        int insert = umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
        return insert;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public int delMemberReceiveAddress(String memberReceiveAddressId) {
        int del = umsMemberReceiveAddressMapper.deleteByPrimaryKey(memberReceiveAddressId);
        return del;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public int updMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        int upd = umsMemberReceiveAddressMapper.updateByPrimaryKey(umsMemberReceiveAddress);
        return upd;
    }

    @Override
    public UmsMemberReceiveAddress getMemberReceiveAddress(String memberReceiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = umsMemberReceiveAddressMapper.selectByPrimaryKey(memberReceiveAddressId);
        return umsMemberReceiveAddress;

    }

    @Override
    public  List<UmsMemberReceiveAddress> getMemberReceiveAddressByMemberId(String memberId) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        example.createCriteria().andEqualTo("memberId", memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);
        return umsMemberReceiveAddresses;
    }

    @Override
    public List<UmsMemberReceiveAddress> getMemberReceiveAddressAll() {
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressesList = umsMemberReceiveAddressMapper.selectAll();
        return umsMemberReceiveAddressesList;
    }


}
