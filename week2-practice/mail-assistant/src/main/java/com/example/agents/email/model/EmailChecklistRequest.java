package com.example.agents.email.model;

import jakarta.validation.constraints.NotBlank;

public class EmailChecklistRequest {

    @NotBlank
    private String englishMail;

    public EmailChecklistRequest() {
    }

    public EmailChecklistRequest(String englishMail) {
        this.englishMail = englishMail;
    }

    public String getEnglishMail() {
        return englishMail;
    }

    public void setEnglishMail(String englishMail) {
        this.englishMail = englishMail;
    }
}

