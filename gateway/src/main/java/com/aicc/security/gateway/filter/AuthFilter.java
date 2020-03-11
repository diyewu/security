package com.aicc.security.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*Mono<User> userMono = ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class);

        System.out.println(userMono);*/
      /*  Mono<Object> map = ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal);
        System.out.println(map);
*/

        Mono<Void> voidMono = ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .map(authentication -> (OAuth2AuthenticationToken) authentication)
                .map(OAuth2AuthenticationToken::getPrincipal)
                .map(
                        bearerToken -> {
                            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
                            builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
                            ServerHttpRequest request = builder.build();
                            return exchange.mutate().request(request).build();
                        })
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
        return voidMono;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
