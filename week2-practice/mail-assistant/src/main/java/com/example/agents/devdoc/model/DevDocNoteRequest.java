package com.example.agents.devdoc.model;

import jakarta.validation.constraints.NotBlank;

public class DevDocNoteRequest {

    @NotBlank
    private String topic;

    private String persona;

    public DevDocNoteRequest() {
    }

    public DevDocNoteRequest(String topic, String persona) {
        this.topic = topic;
        this.persona = persona;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }
}

