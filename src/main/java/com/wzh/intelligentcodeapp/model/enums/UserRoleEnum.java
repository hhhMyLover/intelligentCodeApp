package com.wzh.intelligentcodeapp.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("user", "普通用户"),
    ADMIN("admin", "管理员");

    private final String role;

    private final String description;

    UserRoleEnum(String role, String description) {
        this.role = role;
        this.description = description;
    }

    /**
     * 根据角色获取角色名称
     *
     * @param role
     * @return
     */
    public static UserRoleEnum getEnumByRole(String role) {
        for (UserRoleEnum value : UserRoleEnum.values()) {
            if (value.getRole().equals(role)) {
                return value;
            }
        }
        return null;
    }
}
