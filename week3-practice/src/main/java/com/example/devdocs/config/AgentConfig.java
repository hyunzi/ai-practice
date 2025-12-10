package com.example.devdocs.config;

import com.example.devdocs.agent.DevDocsAssistant;
import com.example.devdocs.tool.DevDocsTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfig {

    @Bean
    public DevDocsAssistant devDocsAssistant(ChatLanguageModel chatLanguageModel, DevDocsTools tools) {
        return AiServices.builder(DevDocsAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(tools)
                .build();
    }
}
