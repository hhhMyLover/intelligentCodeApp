package com.wzh.intelligentcodeapp.ai;

import com.wzh.intelligentcodeapp.ai.model.HtmlCodeResult;
import com.wzh.intelligentcodeapp.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;


public interface AiGeneratorService {

    @SystemMessage(fromResource = "prompt/html-sys-prompt.txt")
    HtmlCodeResult generateHTMLCode(String prompt);

    @SystemMessage(fromResource = "prompt/project-sys-prompt.txt")
    MultiFileCodeResult generateProjectCode(String prompt);


    @SystemMessage(fromResource = "prompt/html-sys-prompt.txt")
    Flux<String> generateHTMLCodeStream(String prompt);

    @SystemMessage(fromResource = "prompt/project-sys-prompt.txt")
    Flux<String> generateProjectCodeStream(String prompt);

    @SystemMessage(fromResource = "prompt/app-name-sys-prompt.txt")
    String generateAppName(String prompt);
}
