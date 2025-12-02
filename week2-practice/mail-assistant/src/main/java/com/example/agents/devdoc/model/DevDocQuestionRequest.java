package com.example.agents.devdoc.model;

import jakarta.validation.constraints.NotBlank;

public class DevDocQuestionRequest {

    @NotBlank
    private String question;

    private String persona;

    private String topicHint;

    public DevDocQuestionRequest() {
    }

    public DevDocQuestionRequest(String question, String persona, String topicHint) {
        this.question = question;
        this.persona = persona;
        this.topicHint = topicHint;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getTopicHint() {
        return topicHint;
    }

    public void setTopicHint(String topicHint) {
        this.topicHint = topicHint;
    }
}

