package com.aicc.security.uaajwt.vo;


public class RoleVO extends Base {



    /**
     * 角色名(中文)
     */
    private String name;

    /**
     * 角色名
     */
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
