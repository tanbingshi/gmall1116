package com.gmall.passport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PassportController {


    @RequestMapping("index")
    public String index(String originUrl, ModelMap modelMap){
        modelMap.put("originUrl",originUrl);
        return "index";
    }

    @ResponseBody
    @RequestMapping("login")
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response){

        Cookie cookie = new Cookie("oldToken", "test");
        response.addCookie(cookie);


        //验证username, password

        return "token";
    }

    @ResponseBody
    @RequestMapping("verity")
    public String verity(String token, HttpServletRequest request){
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");

        return "success";
    }

}
