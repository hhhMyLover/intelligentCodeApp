package com.wzh.intelligentcodeapp.core.save;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;

public class HtmlFileCodeSave extends CodeSaveFileTemplate<HtmlCodeResult> {
    @Override
    protected void saveFile(HtmlCodeResult result, String dirPath) {
        writeFile(dirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected String getBizType() {
        return CodeGenTypeEnum.HTML.getValue();
    }

    @Override
    protected void validateParam(HtmlCodeResult result) {
        super.validateParam(result);
        if (StrUtil.isEmpty(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "HTML代码为空");
        }
    }
}
