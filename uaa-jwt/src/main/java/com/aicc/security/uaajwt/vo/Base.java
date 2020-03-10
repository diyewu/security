package com.aicc.security.uaajwt.vo;

import java.io.Serializable;

public class Base implements Serializable {

    private static final long serialVersionUID = -7519418012137093264L;

    protected Integer id;

    /**
     * 添加时间
     */
    protected Long createdTime;


    /**
     * 描述
     */
    protected String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
