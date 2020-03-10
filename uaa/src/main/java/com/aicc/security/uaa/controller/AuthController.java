package com.aicc.security.uaa.controller;

import com.aicc.security.uaa.domain.Result.ResponseResult;
import com.aicc.security.uaa.entity.AuthUser;
import com.aicc.security.uaa.entity.LoginUserDTO;
import com.aicc.security.uaa.service.RoleService;
import com.aicc.security.uaa.service.UserService;
import com.aicc.security.uaa.utils.RedisUtil;
import com.aicc.security.uaa.utils.ResultUtils;
import com.aicc.security.uaa.vo.LoginUserVO;
import com.aicc.security.uaa.vo.ResponseVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @description 用户权限管理
 */
@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private RedisTokenStore redisTokenStore;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * @description 添加用户
     * @param userDTO
     * @return
     */
    @PostMapping("user")
    public ResponseVO add(@Valid @RequestBody AuthUser userDTO) throws Exception {
        userService.addUser(userDTO);
        return ResponseVO.success();
    }

    /**
     * @description 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("user/{id}")
    public ResponseVO deleteUser(@PathVariable("id")String id) throws Exception {
        userService.deleteUser(id);
        return ResponseVO.success();
    }

    /**
     * @descripiton 修改用户
     * @param userDTO
     * @return
     */
    @PutMapping("user")
    public ResponseVO updateUser(@Valid @RequestBody AuthUser userDTO){
        userService.updateUser(userDTO);
        return ResponseVO.success();
    }

    /**
     * @description 获取用户列表
     * @return
     */
    @GetMapping("getUser")
    public ResponseResult findUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResultUtils.success(authentication);
    }

    /**
     * @description 用户登录
     * @param loginUserDTO
     * @return
     */
    @PostMapping("user/login")
    public ResponseResult login(LoginUserDTO loginUserDTO) throws JsonProcessingException {
        LoginUserVO login = userService.login(loginUserDTO);
        return ResultUtils.success(login);
    }
    /**
     * @description 用户注销
     * @param authorization
     * @return
     */
//    @GetMapping("user/logout")
//    public ResponseVO logout(@RequestHeader("Authorization") String authorization){
//        redisTokenStore.removeAccessToken(AssertUtils.extracteToken(authorization));
//        return ResponseVO.success();
//    }

    /**
     * @description 获取所有角色列表
     * @return
     */
    @GetMapping("role")
    public ResponseVO findAllRole(){
        return roleService.findAllRoleVO();
    }

    /**
     * @description 用户登录
     * @param loginUserDTO
     * @return
     */
    @PostMapping("user/SSOLogin")
    public ResponseResult SSOLogin(LoginUserDTO loginUserDTO) throws JsonProcessingException {
        /**
         * 根据当前登录的用户token获取新的客户端token，相当于单点登录
         * 利用OAuth2.0的客户端密码模式
         */
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();

        String tokenValue = "";
        if(details != null){
            String s = objectMapper.writeValueAsString(details);
            Map<String,Object> map = objectMapper.readValue(s, Map.class);
            tokenValue = map.get("tokenValue") == null ? "" : (String) map.get("tokenValue");
        }
        if(StringUtils.isNotBlank(tokenValue)){
            LoginUserVO loginUserVO = redisUtil.get(tokenValue, LoginUserVO.class);
            loginUserDTO.setUserName(loginUserVO.getUserName());
            loginUserDTO.setPassword(loginUserVO.getPassword());
            loginUserDTO.setAppId(loginUserDTO.getAppId());
            loginUserDTO.setAppSecret(getAppSecret(loginUserDTO.getAppId()));
        }
        LoginUserVO login = userService.SSOLogin(loginUserDTO);
        return ResultUtils.success(login);
    }

    private String getAppSecret(String appId){
        String appSecret = "123456";
        //TODO 补充获取appSecret 逻辑
        return appSecret;
    }

}
