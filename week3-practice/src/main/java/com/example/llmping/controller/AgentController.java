package com.example.llmping.controller;

import com.example.llmping.agent.DevDocsAssistant;
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
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> payload) {
        String question = payload.getOrDefault("question", "");
        String answer = assistant.answer(question);
        return ResponseEntity.ok(Map.of("answer", answer));
    }
}
