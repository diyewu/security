package com.aicc.security.uaajwt.config;

import com.aicc.security.uaajwt.config.handler.SelfAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SelfAuthenticationProvider provider; // 自定义安全认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
        auth.authenticationProvider(provider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
                .authorizeRequests()
                .antMatchers("/auth/user/login","/.well-known/jwks.json").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
    }*/

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

