package com.gmall.api.service;

import com.gmall.api.bean.UmsMember;

import java.util.List;

public interface MemberService {
    int addMember(UmsMember umsMember);

    int delMember(String memberId);

    int updMember(UmsMember member);

    UmsMember getMember(String memberId);

    List<UmsMember> getAllMember();
}
