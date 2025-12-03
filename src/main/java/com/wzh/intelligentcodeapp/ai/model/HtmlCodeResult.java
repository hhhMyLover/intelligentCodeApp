package com.wzh.intelligentcodeapp.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Description("生成HTML代码结果")
@Data
public class HtmlCodeResult {

    @Description("HTML代码")
    private String htmlCode;

    @Description("HTML代码描述")
    private String description;
}
