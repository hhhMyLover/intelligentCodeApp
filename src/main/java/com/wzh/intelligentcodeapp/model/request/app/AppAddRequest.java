package com.wzh.intelligentcodeapp.model.request.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建应用请求
 *
 * @author wzh
 */
@Data
public class AppAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用初始化的 prompt（必填）
     */
    private String initPrompt;
}
