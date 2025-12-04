package com.wzh.intelligentcodeapp.core;

import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSaveFileTest {

    @Resource
    private CodeSaveFile codeSaveFile;

    @Test
    void saveHTMLCode() {
        HtmlCodeResult htmlCodeResult = new HtmlCodeResult();
        htmlCodeResult.setHtmlCode("<html>");
        htmlCodeResult.setDescription("这是HTML代码");
        codeSaveFile.saveHTMLCode(htmlCodeResult);
    }

    @Test
    void saveProjectCode() {
        MultiFileCodeResult multiFileCodeResult = new MultiFileCodeResult();
        multiFileCodeResult.setHtmlCode("<html>");
        multiFileCodeResult.setCssCode("<style>");
        multiFileCodeResult.setJsCode("<script>");
        multiFileCodeResult.setDescription("这是项目工程多文件代码");
        codeSaveFile.saveProjectCode(multiFileCodeResult);
    }
}