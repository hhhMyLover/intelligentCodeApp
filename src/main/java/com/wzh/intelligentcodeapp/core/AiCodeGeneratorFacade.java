package com.wzh.intelligentcodeapp.core;

import com.wzh.intelligentcodeapp.ai.AiGeneratorService;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AiCodeGeneratorFacade {

    @Resource
    private AiGeneratorService aiGeneratorService;

    @Resource
    private CodeSaveFile codeSaveFile;

    public File generateAndSaveCode(String prompt, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入生成模式");
        }
        return switch (codeGenType) {
            case HTML -> generateAndSaveHtmlCode(prompt);
            case MULTI_FILE -> generateAndSaveProjectCode(prompt);
            default -> {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入正确的生成模式");
            }
        };
    }

    private File generateAndSaveProjectCode(String prompt) {
        MultiFileCodeResult projectCode = aiGeneratorService.generateProjectCode(prompt);
        return codeSaveFile.saveProjectCode(projectCode);
    }

    private File generateAndSaveHtmlCode(String prompt) {
        HtmlCodeResult htmlCodeResult = aiGeneratorService.generateHTMLCode(prompt);
        return codeSaveFile.saveHTMLCode(htmlCodeResult);
    }
}
