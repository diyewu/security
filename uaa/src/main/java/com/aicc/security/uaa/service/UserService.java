package com.aicc.security.uaa.service;


import com.aicc.security.uaa.entity.AuthUser;
import com.aicc.security.uaa.entity.LoginUserDTO;
import com.aicc.security.uaa.vo.LoginUserVO;

import java.util.List;

/**
 * @description 用户业务接口
 */
public interface UserService {

    /**
     * @description 添加用户
     */
    void addUser(AuthUser authUser) throws Exception;

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(String id) throws Exception;

    /**
     * @description 修改用户信息
     * @param userDTO
     */
    void updateUser(AuthUser authUser);

    /**
     * @description 获取所有用户列表VO
     * @return
     */
    List<AuthUser> findAllUserVO();

    /**
     * @description 用户登录
     * @return
     */
    LoginUserVO login(LoginUserDTO loginUserDTO);

    /**
     * @description 三方TOKEN申请（单点登录）
     * @return
     */
    LoginUserVO SSOLogin(LoginUserDTO loginUserDTO);


}
