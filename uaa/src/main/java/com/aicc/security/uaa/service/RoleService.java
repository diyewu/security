package com.aicc.security.uaa.service;


import com.aicc.security.uaa.entity.AuthRole;
import com.aicc.security.uaa.vo.ResponseVO;

/**
 * @description 角色管理接口
 */
public interface RoleService {

    /**
     * @description 获取角色列表
     * @return
     */
    ResponseVO findAllRoleVO();

    /**
     * @description 根据角色id获取角色
     */
    AuthRole findById(String id);
}
