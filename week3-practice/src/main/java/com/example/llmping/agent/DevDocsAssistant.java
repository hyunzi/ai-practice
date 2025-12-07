package com.example.llmping.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface DevDocsAssistant {

    @SystemMessage("""
            You are a helpful assistant that can search Stripe devdocs and draft friendly answers.
            Use tools when needed. Keep answers concise unless more detail is requested.
            """)
    String answer(@UserMessage String userMessage);
}
