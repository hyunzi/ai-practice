package com.example.agents.api;

import com.example.agents.api.ChatDtos.ChatRequest;
import com.example.agents.api.ChatDtos.ChatResponse;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class MailAssistantController {

    private final ChatLanguageModel chatLanguageModel;

    public MailAssistantController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        String prompt = """
                You are an email writing assistant.
                Write a polite and clear email in Korean based on the following user input.
                User input:
                %s
                """.formatted(request.message());

        String answer = chatLanguageModel.generate(prompt);
        return ResponseEntity.ok(new ChatResponse(answer));
    }
}

