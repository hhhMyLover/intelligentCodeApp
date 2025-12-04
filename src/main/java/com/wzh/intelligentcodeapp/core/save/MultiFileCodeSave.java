package com.wzh.intelligentcodeapp.core.save;

import cn.hutool.core.util.StrUtil;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;

public class MultiFileCodeSave extends CodeSaveFileTemplate<MultiFileCodeResult> {


    @Override
    protected void saveFile(MultiFileCodeResult result, String dirPath) {
        writeFile(dirPath, "index.html", result.getHtmlCode());
        writeFile(dirPath, "style.css", result.getCssCode());
        writeFile(dirPath, "script.js", result.getJsCode());
    }

    @Override
    protected String getBizType() {
        return CodeGenTypeEnum.MULTI_FILE.getValue();
    }

    @Override
    protected void validateParam(MultiFileCodeResult result) {
        super.validateParam(result);
        if (StrUtil.isEmpty(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "HTML代码为空");
        }
    }
}
