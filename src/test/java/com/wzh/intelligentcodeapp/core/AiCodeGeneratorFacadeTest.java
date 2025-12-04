package com.wzh.intelligentcodeapp.core;

import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

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
}