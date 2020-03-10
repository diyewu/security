package com.aicc.security.uaa.config.AjaxSecurity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 自定义账号密码验证
 */
@Configuration
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserDetailsService securityUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserDetails userInfo = securityUserService.loadUserByUsername(userName);
        if(userInfo == null){
            throw new BadCredentialsException("用户不存在，请重新登陆！");
        }

        boolean checkpw = BCrypt.checkpw(password, userInfo.getPassword());

        /**
         * TODO 增加特殊校验，如果密码与数据库一致也可通过
         */
        if (!checkpw && StringUtils.equals(password,userInfo.getPassword())) {
            checkpw = true;
        }
        if (!checkpw) {
            throw new BadCredentialsException("用户名密码不正确，请重新登陆！");
        }

        return new UsernamePasswordAuthenticationToken(userName, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
