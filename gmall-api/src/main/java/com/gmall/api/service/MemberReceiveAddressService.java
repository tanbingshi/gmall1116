package com.gmall.api.service;

import com.gmall.api.bean.UmsMemberReceiveAddress;
import com.gmall.api.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface MemberReceiveAddressService {
    int addMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    int delMemberReceiveAddress(String memberReceiveAddressId);

    int updMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);

    UmsMemberReceiveAddress getMemberReceiveAddress(String memberReceiveAddressId);

    List<UmsMemberReceiveAddress> getMemberReceiveAddressByMemberId(String memberId);

    List<UmsMemberReceiveAddress> getMemberReceiveAddressAll();
}
