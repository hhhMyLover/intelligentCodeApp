package com.wzh.intelligentcodeapp.constant;

import lombok.Getter;

@Getter
public enum UserConstant {

    /**
     * 默认用户
     */
    DEFAULT_USER("user"),
    /**
     * 管理员用户
     */
    ADMIN_USER("admin"),
    /**
     * 用户登录状态
     */
    USER_LOGIN_STATUS("user_login");

    private final String value;

    UserConstant(String value) {
        this.value = value;
    }
}
