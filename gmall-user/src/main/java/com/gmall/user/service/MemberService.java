package com.gmall.user.service;

import com.gmall.user.bean.UmsMember;

import java.lang.reflect.Member;
import java.util.List;

public interface MemberService {
    int addMember(UmsMember umsMember);

    int delMember(String memberId);

    int updMember(UmsMember member);

    UmsMember getMember(String memberId);

    List<UmsMember> getAllMember();
}
