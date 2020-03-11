package com.aicc.security.uaajwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @description 资源服务器
 */
@Configuration
@EnableResourceServer
public  class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //请求权限配置
                .authorizeRequests()
                //下边的路径放行,不需要经过认证
                .antMatchers("/oauth/*","/demo/**", "/auth/user/login","/.well-known/jwks.json").permitAll()
                //OPTIONS请求不需要鉴权
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //获取角色 权限列表接口只允许系统管理员及高级用户访问
//                .antMatchers(HttpMethod.GET, "/auth/role").hasAnyAuthority(ROLE_ADMIN)
                //其余接口没有角色限制，但需要经过认证，只要携带token就可以放行
                .anyRequest()
                .authenticated();

    }
}
