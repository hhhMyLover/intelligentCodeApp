package com.wzh.intelligentcodeapp.core;

import cn.hutool.core.lang.Assert;
import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeParseTest {

    @Test
    void parseHtmlCode() {
        String codeContent = "```html\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>三毛的一生</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>三毛的一生</h1>\n" +
                "    <p>三毛的一生</p>\n" +
                "</body>\n" +
                "</html>\n" +
                "```";
        HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(codeContent);

        Assert.notNull(htmlCodeResult);

    }

    @Test
    void parseMultiFileCode() {
        String codeContent = "```html\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>三毛的一生</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>三毛的一生</h1>\n" +
                "    <p>三毛的一生</p>\n" +
                "</body>\n" +
                "</html>\n" +
                "```\n" +
                "\n" +
                "```css\n" +
                "body {\n" +
                "    font-family: Arial, sans-serif;\n" +
                "    background-color: #f0f0f0;\n" +
                "}\n" +
                "\n" +
                "h1 {\n" +
                "    color: #333;\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "```javascript\n" +
                "console.log('三毛的一生');\n" +
                "document.addEventListener('DOMContentLoaded', function() {\n" +
                "    console.log('页面加载完成');\n" +
                "});\n" +
                "```";
        MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(codeContent);
        Assert.notNull(multiFileCodeResult);
    }
}