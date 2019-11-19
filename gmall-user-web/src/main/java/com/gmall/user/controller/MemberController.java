package com.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.UmsMember;
import com.gmall.api.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemberController {

    @Reference
    MemberService memberService;

    @RequestMapping(value = "member/add", method = RequestMethod.POST)
    @ResponseBody
    public String addMember(@RequestBody UmsMember umsMember){
        int  insert = memberService.addMember(umsMember);
        if(insert > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "member/del/{memberId}", method = RequestMethod.GET)
    public String delMember(@PathVariable("memberId") String memberId){
        int del = memberService.delMember(memberId);
        if(del > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "member/upd", method = RequestMethod.GET)
    public String updMember(@RequestBody UmsMember member){
        int upd = memberService.updMember(member);
        if(upd > 0){
            return "success";
        }else{
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "member/get", method = RequestMethod.GET)
    public UmsMember getMember(@RequestParam("memberId") String memberId){
        UmsMember umsMember = memberService.getMember(memberId);
        if(umsMember == null){
            return null;
        }
        return umsMember;
    }

    @ResponseBody
    @RequestMapping(value = "member/getAll", method = RequestMethod.GET)
    public List<UmsMember> getAllMember(){
      return  memberService.getAllMember();
    }
}
