package com.aicc.security.uaajwt.service;


import com.aicc.security.uaajwt.entity.LoginUserDTO;
import com.aicc.security.uaajwt.entity.Token;

/**
 * @description 用户业务接口
 */
public interface UserService {


    /**
     * @description 用户登录
     * @return
     */
    Token login(LoginUserDTO loginUserDTO);



}
