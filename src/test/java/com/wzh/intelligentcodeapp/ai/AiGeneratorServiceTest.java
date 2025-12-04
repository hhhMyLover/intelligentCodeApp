package com.wzh.intelligentcodeapp.ai;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Assert;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.Buffer;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class AiGeneratorServiceTest {

    @Autowired
    private AiGeneratorService aiGeneratorService;

    @Test
    void generateHTMLCode() {
        HtmlCodeResult htmlCode = aiGeneratorService.generateHTMLCode("请生成一个HTML页面，主题是三毛的一生");
        assertNotNull(htmlCode);
        String property = System.getProperty("user.dir") + "/tem";
        String dir = property + "/html/genHTML.html";
        File file = FileUtil.file(dir);
        FileUtil.writeUtf8String(htmlCode.getHtmlCode(), file);
        log.info("生成文件成功：{}", file.getAbsolutePath());
        log.info(htmlCode.getHtmlCode());
    }

    @Test
    void generateProjectCode() {
        MultiFileCodeResult projectCode = aiGeneratorService.generateProjectCode("请你帮我生成一个vue项目，主题是三毛的一生");
        assertNotNull(projectCode);
        String property = System.getProperty("user.dir") + "/tem";
        String dir = property + "/project/genProject.zip";
        File file = FileUtil.file(dir);
        FileUtil.writeUtf8String(projectCode.getCssCode()+projectCode.getHtmlCode()+projectCode.getJsCode(), file);
        log.info("生成文件成功：{}", file.getAbsolutePath());
        log.info("projectCode:{}",projectCode);
    }

    @Test
    void generateName() {
        String generateAppName = aiGeneratorService.generateAppName("自制饮品教程（咖啡、奶茶、气泡水）、食材采购清单");
        Assert.notNull(generateAppName);
    }
}