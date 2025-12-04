package com.wzh.intelligentcodeapp.model.request.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新应用请求
 *
 * @author wzh
 */
@Data
public class AppUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;
}
