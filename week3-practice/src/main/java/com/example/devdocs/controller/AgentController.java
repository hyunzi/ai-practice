package com.example.devdocs.controller;

import com.example.devdocs.agent.DevDocsAssistant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final DevDocsAssistant assistant;

    public AgentController(DevDocsAssistant assistant) {
        this.assistant = assistant;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody AskRequest payload) {
        String question = payload.question() == null ? "" : payload.question();
        String answer = assistant.answer(question);
        return ResponseEntity.ok(Map.of("answer", answer));
    }

    public record AskRequest(String question, String mode) {}
}
