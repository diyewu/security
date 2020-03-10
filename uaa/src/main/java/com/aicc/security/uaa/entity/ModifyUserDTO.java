package com.aicc.security.uaa.entity;


/**
 * @description 用户修改密码传输参数
 */
public class ModifyUserDTO {

    /**
     * 原密码
     */
    String oldPassword;

    /**
     * 新密码
     */
    String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
