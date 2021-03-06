package com.aicc.security.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ResourceServerConfigurer {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
//                .pathMatchers("/auth/**").permitAll()
                .anyExchange()
                .authenticated()
                .and().csrf().disable();

        http.oauth2ResourceServer().jwt();

        return http.build();
    }
}
