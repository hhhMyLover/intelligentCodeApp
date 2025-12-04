package com.wzh.intelligentcodeapp.core;

import com.wzh.intelligentcodeapp.ai.AiGeneratorService;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Service
@Slf4j
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

    public Flux<String> generateAndSaveCodeStream(String prompt, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入生成模式");
        }
        return switch (codeGenType) {
            case HTML -> generateAndSaveHtmlCodStream(prompt);
            case MULTI_FILE -> generateAndSaveProjectCodeStream(prompt);
            default -> {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入正确的生成模式");
            }
        };
    }

    private Flux<String> generateAndSaveHtmlCodStream(String prompt) {
        Flux<String> htmlCodeStream = aiGeneratorService.generateHTMLCodeStream(prompt);
        StringBuilder result = new StringBuilder();
        return htmlCodeStream.doOnNext(result::append)
                .doOnComplete(() -> {
                    try {
                        File file = codeSaveFile.saveHTMLCode(CodeParser.parseHtmlCode(result.toString()));
                        log.info("保存文件成功：{}", file.getAbsolutePath());
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存文件失败");
                    }
                });
    }

    private Flux<String> generateAndSaveProjectCodeStream(String prompt) {
        Flux<String> projectCodeStream = aiGeneratorService.generateProjectCodeStream(prompt);
        StringBuilder result = new StringBuilder();
        return projectCodeStream.doOnNext(result::append)
                .doOnComplete(() -> {
                    try {
                        File file = codeSaveFile.saveProjectCode(CodeParser.parseMultiFileCode(result.toString()));
                        log.info("保存文件成功：{}", file.getAbsolutePath());
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存文件失败");
                    }
                });
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
