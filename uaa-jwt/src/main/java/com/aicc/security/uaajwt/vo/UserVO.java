package com.aicc.security.uaajwt.vo;


public class UserVO  {

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户角色
     */
    private RoleVO role;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleVO getRole() {
        return role;
    }

    public void setRole(RoleVO role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserVO{" +
               "account='" + account + '\'' +
               ", name='" + name + '\'' +
               ", password='" + password + '\'' +
               ", role=" + role +
               '}';
    }
}
