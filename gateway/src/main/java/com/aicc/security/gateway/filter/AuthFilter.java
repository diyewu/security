package com.aicc.security.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication.getPrincipal());
        /*String url = exchange.getRequest().getURI().getPath();
        //跳过不需要验证的路径(白名单）
        if(null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)){
            return chain.filter(exchange);
        }
        //1.获取请求
        ServerHttpRequest request = exchange.getRequest();
        //2.获取响应
        ServerHttpResponse response = exchange.getResponse();
        //3.判断用户是否访问的为登录路径，如果是登录路径则放行
        if (url.contains(LoginUrl)){
            return chain.filter(exchange);
        }
        //4. 获取请求头中的令牌
        String token = request.getHeaders().getFirst(TOKEN);
        //5. 如果请求头中的令牌为空, 则返回错误状态码
        if (StringUtils.isEmpty(token)){
            throw new BusinessException(EnumException.SERVICE_TOKEN_ISNULL);
        }
        //6. 解析请求头中的jwt令牌
        try {
            UserPermission userPermission = JwtUtil.parseJwt(token);
            String userId = String.valueOf(userPermission.getId());
            String tokenFromRedis = (String)redisUtil.get(userId);
            if(tokenFromRedis != null) {
                if(tokenFromRedis.equals(token)){
                    Map<Object,Object> resourceMap = redisUtil.hashMapGet(PREFIX + userId);
                    List<String> urlList = new ArrayList<String>();
                    for(Object resource : resourceMap.values()){
                        urlList.add(resource.toString());
                    }
                    if(urlList.contains(url)){
                        return chain.filter(exchange);
                    }else {
                        throw new BusinessException(EnumException.SERVICE_TOKEN_REJECTED);
                    }
                }
                else{
                    throw new BusinessException(300997,"您的账号在别地登录");
                }
            }else {
                throw new BusinessException(300998,"您已被强制下线");
            }
        } catch (Exception e) {
            //7. 如果解析出错, 说明令牌过期或者被篡改, 返回状态码
            e.printStackTrace();
            throw new BusinessException(EnumException.SERVICE_TOKEN_EXPIRED);
        }*/
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
