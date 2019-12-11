package com.gmall.intercept;

import com.gmall.annotation.LoginAnnotation;
import com.gmall.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginIntercept  extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginAnnotation loginAnnotation = handlerMethod.getMethodAnnotation(LoginAnnotation.class);

        //不需要登录或者检查登录
        if(loginAnnotation == null){
            return true;
        }

        boolean loginSuccess = loginAnnotation.loginSuccess();

        String oldToken = "";
        String newToken = "";

        String token = "";

        //判断是否登录过
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if(name.equals("oldToken")){
                    oldToken = cookie.getValue();
                }else if(name.equals("newToken")){
                    newToken = cookie.getValue();
                }
            }
        }

        //获取token并判断是否已经登录过
        if(StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }

        if(StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        //判断是否需要登录
        if(loginSuccess){
            //说明需要登录
            if(StringUtils.isNotBlank(token)){
                //说明登录过
                //拿token去认证中心
                String result = HttpClientUtil.doGet("http://127.0.0.1:8085/verity?token=" + token);
                //设置member属性带请求域
                if(StringUtils.isNotBlank(result)){
                    request.setAttribute("memberId","1");
                    request.setAttribute("nickname","test");
                    //覆盖token
                    Cookie cookie = new Cookie("oldToken", token);
                    cookie.setMaxAge(60 * 30);
                    response.addCookie(cookie);
                    return true;
                }
            }else{
                //说明没有登录过
                //重定向到登录页面
                StringBuffer originUrl = request.getRequestURL();
                response.sendRedirect("http://127.0.0.1:8085/index?originUrl="+originUrl.toString());
                return false;
            }
        }else{
            //该请求不需要登录
            //检查是否登录过
            if(StringUtils.isNotBlank(token)){
                request.setAttribute("memberId","1");
                request.setAttribute("nickname","test");
                //覆盖token
                Cookie cookie = new Cookie("oldToken", token);
                cookie.setMaxAge(60 * 30);
                response.addCookie(cookie);
            }
        }

        return true;
    }
}
