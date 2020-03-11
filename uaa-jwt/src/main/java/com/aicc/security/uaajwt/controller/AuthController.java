package com.aicc.security.uaajwt.controller;

import com.aicc.security.uaajwt.entity.LoginUserDTO;
import com.aicc.security.uaajwt.entity.Token;
import com.aicc.security.uaajwt.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 用户权限管理
 */
@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("hello")
    public String getRequest() {
        return "Hello World.";
    }

    /**
     * @description 用户登录
     * @param loginUserDTO
     * @return
     */
    @PostMapping("user/login")
    public Token login(LoginUserDTO loginUserDTO) throws JsonProcessingException {
        Token token = userService.login(loginUserDTO);
        return token;
    }

    /**
     * @description 用户登录
     * @param loginUserDTO
     * @return
     */
    @PostMapping("user/refreshToken")
    public Token refreshToken(LoginUserDTO loginUserDTO) throws JsonProcessingException {
        Token token = userService.login(loginUserDTO);
        return token;
    }

}
