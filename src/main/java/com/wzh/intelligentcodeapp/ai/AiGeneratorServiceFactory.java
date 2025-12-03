package com.wzh.intelligentcodeapp.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiGeneratorServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Bean
    public AiGeneratorService createAiGeneratorService(){
        return AiServices.builder(AiGeneratorService.class)
                .chatModel(chatModel)
                .build();
    }
}
