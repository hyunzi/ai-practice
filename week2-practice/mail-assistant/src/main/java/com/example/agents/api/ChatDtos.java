package com.example.agents.api;

import jakarta.validation.constraints.NotBlank;

public class ChatDtos {

    public record ChatRequest(
            @NotBlank(message = "message는 필수입니다.")
            String message
    ) {
    }

    public record ChatResponse(String reply) {
    }
}

