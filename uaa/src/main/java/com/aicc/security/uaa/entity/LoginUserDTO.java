package com.aicc.security.uaa.entity;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @description 登录用户传输参数
 */
@JsonIgnoreProperties
public class LoginUserDTO {

    /**
     * 用户名
     */
    private String userName;


    /**
     * 用户密码
     */
    private String password;

    /**
     * 客户端ID
     */
    private String appId;

    /**
     * 客户端密钥
     */
    private String appSecret;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
