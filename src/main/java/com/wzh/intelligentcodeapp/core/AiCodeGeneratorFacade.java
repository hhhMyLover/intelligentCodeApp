package com.wzh.intelligentcodeapp.core;

import com.wzh.intelligentcodeapp.ai.AiGeneratorService;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import com.wzh.intelligentcodeapp.core.parse.CodeParseExecutor;
import com.wzh.intelligentcodeapp.core.save.CodeSaveFileExecutor;
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

    public File generateAndSaveCode(String prompt, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入生成模式");
        }
        return switch (codeGenType) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiGeneratorService.generateHTMLCode(prompt);
                yield CodeSaveFileExecutor.saveCode(htmlCodeResult, codeGenType);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult projectCode = aiGeneratorService.generateProjectCode(prompt);
                yield CodeSaveFileExecutor.saveCode(projectCode, codeGenType);
            }
            default -> {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的生成模式");
            }
        };
    }

    public Flux<String> generateAndSaveCodeStream(String prompt, CodeGenTypeEnum codeGenType) {
        if (codeGenType == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入生成模式");
        }
        return switch (codeGenType) {
            case HTML -> {
                Flux<String> htmlCodeStream = aiGeneratorService.generateHTMLCodeStream(prompt);
                yield processCodeSteam(htmlCodeStream, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                Flux<String> projectCodeStream = aiGeneratorService.generateProjectCodeStream(prompt);
                yield processCodeSteam(projectCodeStream, CodeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的生成模式");
            }
        };
    }

    private Flux<String> processCodeSteam(Flux<String> codeStream, CodeGenTypeEnum codeGenType) {

        StringBuilder result = new StringBuilder();
        return codeStream.doOnNext(result::append)
                .doOnComplete(() -> {
                    try {
                        Object parseCode = CodeParseExecutor.parseCode(result.toString(), codeGenType);
                        File file = CodeSaveFileExecutor.saveCode(parseCode, codeGenType);
                        log.info("保存文件成功：{}", file.getAbsolutePath());
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存文件失败");
                    }
                });
    }
}
