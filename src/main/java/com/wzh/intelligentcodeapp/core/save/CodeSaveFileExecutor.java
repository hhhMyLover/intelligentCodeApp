package com.wzh.intelligentcodeapp.core.save;

import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;

import java.io.File;

public class CodeSaveFileExecutor {

    public static final HtmlFileCodeSave htmlFileCodeSave = new HtmlFileCodeSave();

    public static final MultiFileCodeSave multiFile = new MultiFileCodeSave();


    public static File saveCode(Object codeResult, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlFileCodeSave.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFile.saveCode((MultiFileCodeResult) codeResult);
            default -> throw new RuntimeException("不支持的生成模式");
        };
    }
}
