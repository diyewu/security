package com.aicc.security.uaa.config;

import com.aicc.security.uaa.config.AjaxSecurity.SelfAuthenticationProvider;
import com.aicc.security.uaa.dao.AuthUserMapper;
import com.aicc.security.uaa.domain.CustomUserDetail;
import com.aicc.security.uaa.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @description Security核心配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthUserMapper authUserMapper;

    @Autowired
    SelfAuthenticationProvider provider; // 自定义安全认证

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
        auth.authenticationProvider(provider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {
            AuthUser authUser = authUserMapper.selectByUserName(username);
            if(authUser == null){
                return null;
            }
            if(authUser != null){
                CustomUserDetail customUserDetail = new CustomUserDetail();
                customUserDetail.setUsername(authUser.getUser_name());
                customUserDetail.setPassword(authUser.getPassword());
                //TODO
                List<GrantedAuthority> list = AuthorityUtils.createAuthorityList("ADMIN");
                customUserDetail.setAuthorities(list);
                return customUserDetail;
            }else {//返回空
                return null;
            }

        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
