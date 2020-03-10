package com.aicc.security.uaajwt.service.impl;

import com.aicc.security.uaajwt.config.ServerConfig;
import com.aicc.security.uaajwt.entity.LoginUserDTO;
import com.aicc.security.uaajwt.entity.Token;
import com.aicc.security.uaajwt.enums.UrlEnum;
import com.aicc.security.uaajwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



@Service
public class UserServiceImpl implements UserService {
    private String CLIENT_ID = "user-service";
    private String CLIENT_SECRET = "123456";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ServerConfig serverConfig;

    @Override
    public Token login(LoginUserDTO loginUserDTO) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", CLIENT_ID);
        paramMap.add("client_secret", CLIENT_SECRET);
        paramMap.add("username", loginUserDTO.getUserName());
        paramMap.add("password", loginUserDTO.getPassword());
        paramMap.add("grant_type", "password");
        Token token = null;
        try {
            //因为oauth2本身自带的登录接口是"/oauth/token"，并且返回的数据类型不能按我们想要的去返回
            //但是我的业务需求是，登录接口是"user/login"，由于我没研究过要怎么去修改oauth2内部的endpoint配置
            //所以这里我用restTemplate(HTTP客户端)进行一次转发到oauth2内部的登录接口，比较简单粗暴
            token = restTemplate.postForObject(serverConfig.getUrl() + UrlEnum.LOGIN_URL.getUrl(), paramMap, Token.class);

        } catch (RestClientException e) {
            try {
                e.printStackTrace();
                //此处应该用自定义异常去返回，在这里我就不去具体实现了
                //throw new Exception("username or password error");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return token;
    }




}
