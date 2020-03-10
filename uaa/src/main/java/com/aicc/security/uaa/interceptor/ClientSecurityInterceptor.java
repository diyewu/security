package com.aicc.security.uaa.interceptor;

import com.aicc.security.uaa.utils.SignUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ClientSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //检查client正确性
        Boolean isVolid = SignUtils.checkSign(request);
        if(!isVolid){
            //TODO 需要通过response来产生响应
            return false;
        }
        return true;

    }
}
