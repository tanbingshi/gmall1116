package com.gmall.user.service.impl;

import com.gmall.user.bean.UmsMember;
import com.gmall.user.mapper.UmsMemberMapper;
import com.gmall.user.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public int addMember(UmsMember umsmember) {
        int insert = umsMemberMapper.insert(umsmember);
        return insert;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public int delMember(String memberId) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("id",memberId);
        int del = umsMemberMapper.deleteByExample(example);
        return del;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public int updMember(UmsMember member) {
        int upd = umsMemberMapper.updateByPrimaryKey(member);
        return upd;
    }

    @Override
    public UmsMember getMember(String memberId) {
        UmsMember umsMember = umsMemberMapper.selectByPrimaryKey(memberId);
        return umsMember;
    }

    @Override
    public List<UmsMember> getAllMember() {
        return umsMemberMapper.selectAll();
    }
}
