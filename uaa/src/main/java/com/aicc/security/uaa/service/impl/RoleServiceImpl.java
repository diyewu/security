package com.aicc.security.uaa.service.impl;

import com.aicc.security.uaa.dao.AuthRoleMapper;
import com.aicc.security.uaa.entity.AuthRole;
import com.aicc.security.uaa.service.RoleService;
import com.aicc.security.uaa.vo.ResponseVO;
import com.aicc.security.uaa.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Override
    public ResponseVO findAllRoleVO() {
        List<AuthRole> rolePOList = authRoleMapper.selectAll();
        List<RoleVO> roleVOList = new ArrayList<>();
        rolePOList.forEach(rolePO->{
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(rolePO,roleVO);
            roleVOList.add(roleVO);
        });
        return ResponseVO.success(roleVOList);
    }

    @Override
    public AuthRole findById(String id) {
        return authRoleMapper.selectByPrimaryKey(id);
    }
}
