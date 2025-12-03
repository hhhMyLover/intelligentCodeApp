package com.wzh.intelligentcodeapp.ai;

import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;


public interface AiGeneratorService {

    @SystemMessage(fromResource = "prompt/html-sys-prompt.txt")
    HtmlCodeResult generateHTMLCode(String prompt);

    @SystemMessage(fromResource = "prompt/project-sys-prompt.txt")
    MultiFileCodeResult generateProjectCode(String prompt);
}
