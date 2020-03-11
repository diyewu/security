package com.aicc.security.uaa.config;

import com.aicc.security.uaa.interceptor.ClientSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    ClientSecurityInterceptor clientSecurityInterceptor;

    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(clientSecurityInterceptor)
                .excludePathPatterns("/auth/user/login","/demo/**")
//                .excludePathPatterns("/authlogin/**")
                .addPathPatterns("/**")
        ;
    }
}*/
