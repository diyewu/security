package com.aicc.security.uaa.domain.bean;


public class RefreshTokenBean {

    /**
     * 过期时间
     */
    private String expiration;
    /**
     * token值
     */
    private String value;

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
