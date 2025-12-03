package com.wzh.intelligentcodeapp.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Description("项目工程多文件代码结果")
@Data
public class MultiFileCodeResult {

    @Description("HTML代码")
    private String htmlCode;

    @Description("CSS代码")
    private String cssCode;

    @Description("JS代码")
    private String jsCode;

    @Description("描述")
    private String description;
}
