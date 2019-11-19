package com.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.UmsMemberReceiveAddress;
import com.gmall.api.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemberReceiveAddressController {

    @Reference
    MemberReceiveAddressService memberReceiveAddressService;

    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/add", method = RequestMethod.POST)
    public String addMemberReceiveAddress(@RequestBody UmsMemberReceiveAddress umsMemberReceiveAddress){
        int add = memberReceiveAddressService.addMemberReceiveAddress(umsMemberReceiveAddress);
        if(add > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/del/{memberReceiveAddressId}", method = RequestMethod.GET)
    public String delMemberReceiveAddress(@PathVariable("memberReceiveAddressId") String memberReceiveAddressId){
        int del = memberReceiveAddressService.delMemberReceiveAddress(memberReceiveAddressId);
        if(del > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/upd", method = RequestMethod.GET)
    public String updMemberReceiveAddress(@RequestBody UmsMemberReceiveAddress umsMemberReceiveAddress){
       int upd = memberReceiveAddressService.updMemberReceiveAddress(umsMemberReceiveAddress);
        if(upd > 0){
            return "success";
        }else{
            return "failed";
        }
    }
    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/get", method = RequestMethod.GET)
    public UmsMemberReceiveAddress getMemberReceiveAddress(@RequestParam("memberReceiveAddressId") String memberReceiveAddressId){
        UmsMemberReceiveAddress umsMemberReceiveAddress = memberReceiveAddressService.getMemberReceiveAddress(memberReceiveAddressId);
        if(umsMemberReceiveAddress == null){
            return null;
        }
        return umsMemberReceiveAddress;
    }

    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/getByMemberId", method = RequestMethod.GET)
    public  List<UmsMemberReceiveAddress> getMemberReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressesList = memberReceiveAddressService.getMemberReceiveAddressByMemberId(memberId);
        if(umsMemberReceiveAddressesList == null || umsMemberReceiveAddressesList.size() == 0){
            return null;
        }
        return umsMemberReceiveAddressesList;
    }

    @ResponseBody
    @RequestMapping(value = "memberReceiveAddress/getAll", method = RequestMethod.GET)
    public List<UmsMemberReceiveAddress> getMemberReceiveAddressAll(){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddressesList = memberReceiveAddressService.getMemberReceiveAddressAll();
        if(umsMemberReceiveAddressesList == null || umsMemberReceiveAddressesList.size() == 0){
            return null;
        }
        return umsMemberReceiveAddressesList;
    }
}
