package com.example.agents.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {

    @Bean(name = "emailAgentModel")
    public ChatLanguageModel emailAgentModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3.2:3b")
                .temperature(0.3)
                .build();
    }

    @Bean(name = "devDocAgentModel")
    public ChatLanguageModel devDocAgentModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3.2:3b")
                .temperature(0.2)
                .build();
    }
}
