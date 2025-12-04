package com.wzh.intelligentcodeapp.core.parse;

import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;

public class CodeParseExecutor {

    public static final HtmFileCodeParse htmFileCodeParse = new HtmFileCodeParse();

    public static final MultiFileCodePare multiFileCodePare = new MultiFileCodePare();


    /**
     * 解析代码执行器
     * @param codeContent 代码内容
     * @param codeGenType 解析格式
     * @return 解析结果
     */
    public static Object parseCode(String codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmFileCodeParse.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodePare.parseCode(codeContent);
            default -> throw new RuntimeException("不支持的生成模式");
        };
    }

}
