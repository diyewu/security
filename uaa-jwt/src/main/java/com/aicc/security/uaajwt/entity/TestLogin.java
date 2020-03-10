package com.aicc.security.uaajwt.entity;

public class TestLogin {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "TestLogin{" +
               "userName='" + userName + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
