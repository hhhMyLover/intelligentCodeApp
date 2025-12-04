package com.wzh.intelligentcodeapp.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CodeSaveFile {
    public static final String ROOT_DIR_PATH = System.getProperty("user.dir") + "/tem/code_output";

    public File saveHTMLCode(HtmlCodeResult result) {
        String dirPath = buildUniqueFileName(CodeGenTypeEnum.HTML.getValue());
        saveFile(dirPath, "index.html", result.getHtmlCode());
        return new File(dirPath);
    }

    public File saveProjectCode(MultiFileCodeResult result) {
        String dirPath = buildUniqueFileName(CodeGenTypeEnum.MULTI_FILE.getValue());
        saveFile(dirPath, "index.html", result.getHtmlCode());
        saveFile(dirPath, "style.css", result.getCssCode());
        saveFile(dirPath, "script.js", result.getJsCode());
        return new File(dirPath);
    }

    private String buildUniqueFileName(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}",bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = ROOT_DIR_PATH + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    private void saveFile(String dirPath, String fileName, String content) {
        File file = new File(dirPath+ File.separator + fileName);
        FileUtil.writeUtf8String(content, file);
    }
}
