package com.wzh.intelligentcodeapp.core;

import cn.hutool.core.lang.Assert;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
//        File file = aiCodeGeneratorFacade.generateAndSaveCode("请生成一个HTML页面，主题是三毛的一生", CodeGenTypeEnum.HTML);

        File file = aiCodeGeneratorFacade.generateAndSaveCode("请生成一个vue项目，主题是三毛的一生", CodeGenTypeEnum.MULTI_FILE);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> fluxResult = aiCodeGeneratorFacade.generateAndSaveCodeStream("请生成一个vue项目，主题是三毛的一生", CodeGenTypeEnum.MULTI_FILE);
        List<String> stringList = fluxResult.collectList().block();
        Assert.notNull(stringList);
        String join = String.join("", stringList);
        Assert.notNull(join);
 }
}