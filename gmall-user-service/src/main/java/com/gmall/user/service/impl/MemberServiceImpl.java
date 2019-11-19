package com.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.UmsMember;
import com.gmall.api.service.MemberService;
import com.gmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Override
    public int addMember(UmsMember umsmember) {
        int insert = umsMemberMapper.insert(umsmember);
        return insert;
    }

    @Override
    public int delMember(String memberId) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("id",memberId);
        int del = umsMemberMapper.deleteByExample(example);
        return del;
    }

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
