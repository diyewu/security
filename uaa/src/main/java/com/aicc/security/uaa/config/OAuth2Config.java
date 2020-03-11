package com.aicc.security.uaa.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.concurrent.TimeUnit;


/**
 * @description OAuth2服务器配置
 */
@Configuration
public class OAuth2Config {

    public static final String ROLE_ADMIN = "ADMIN";
    //访问客户端密钥
    public static final String CLIENT_SECRET = "123456";
    //访问客户端ID
    public static final String CLIENT_ID ="client_1";
    public static final String CLIENT_ID2 ="client_2";
    //鉴权模式
    public static final String GRANT_TYPE[] = {"password","refresh_token"};

    /**
     * @description 资源服务器
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

//        @Autowired
//        private CustomAuthExceptionHandler customAuthExceptionHandler;

        /*@Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.stateless(false)
                    .accessDeniedHandler(customAuthExceptionHandler)
                    .authenticationEntryPoint(customAuthExceptionHandler);
        }*/

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    //请求权限配置
                    .authorizeRequests()
                    //下边的路径放行,不需要经过认证
                    .antMatchers("/oauth/*","/demo/**", "/auth/user/login").permitAll()
                    //OPTIONS请求不需要鉴权
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    //获取角色 权限列表接口只允许系统管理员及高级用户访问
                    .antMatchers(HttpMethod.GET, "/auth/role").hasAnyAuthority(ROLE_ADMIN)
                    //其余接口没有角色限制，但需要经过认证，只要携带token就可以放行
                    .anyRequest()
                    .authenticated();

        }
    }

    /**
     * @description 认证授权服务器
     */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private RedisConnectionFactory connectionFactory;

        @Autowired
        private RedisTokenStore redisTokenStore;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode(CLIENT_SECRET);
            //配置客户端，使用密码模式验证鉴权
            clients.inMemory()
                    .withClient(CLIENT_ID)
                    //密码模式及refresh_token模式
                    .authorizedGrantTypes(GRANT_TYPE[0], GRANT_TYPE[1])
                    .scopes("all")
                    .secret(finalSecret)
                    .and()
                    .withClient(CLIENT_ID2)
                    //密码模式及refresh_token模式
                    .authorizedGrantTypes(GRANT_TYPE[0], GRANT_TYPE[1])
                    .scopes("all")
                    .secret(finalSecret);
        }

        @Bean
        public RedisTokenStore redisTokenStore() {
            return new RedisTokenStore(connectionFactory);
        }

        /**
         * @description token及用户信息存储到redis，当然你也可以存储在当前的服务内存，不推荐
         * @param endpoints
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            //token信息存到redis
            endpoints.tokenStore(redisTokenStore).authenticationManager(authenticationManager);
            //配置TokenService参数
            DefaultTokenServices tokenService = new DefaultTokenServices();
            tokenService.setTokenStore(endpoints.getTokenStore());
            tokenService.setSupportRefreshToken(true);
            tokenService.setClientDetailsService(endpoints.getClientDetailsService());
            tokenService.setTokenEnhancer(endpoints.getTokenEnhancer());
            //1小时
            tokenService.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1));
            //1小时
            tokenService.setRefreshTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(1));
            tokenService.setReuseRefreshToken(false);
            endpoints.tokenServices(tokenService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            //允许表单认证
            oauthServer.allowFormAuthenticationForClients().tokenKeyAccess("isAuthenticated()")
                    .checkTokenAccess("permitAll()");
        }
    }


    /**
     * @description redis模板，存储关键字是字符串，值jackson2JsonRedisSerializer是序列化后的值
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
