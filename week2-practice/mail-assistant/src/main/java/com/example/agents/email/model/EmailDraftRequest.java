package com.example.agents.email.model;

import jakarta.validation.constraints.NotBlank;

public class EmailDraftRequest {

    @NotBlank
    private String koreanDraft;

    private String tone;

    private String mailType;

    private String additionalContext;

    public EmailDraftRequest() {
    }

    public EmailDraftRequest(String koreanDraft, String tone, String mailType, String additionalContext) {
        this.koreanDraft = koreanDraft;
        this.tone = tone;
        this.mailType = mailType;
        this.additionalContext = additionalContext;
    }

    public String getKoreanDraft() {
        return koreanDraft;
    }

    public void setKoreanDraft(String koreanDraft) {
        this.koreanDraft = koreanDraft;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getAdditionalContext() {
        return additionalContext;
    }

    public void setAdditionalContext(String additionalContext) {
        this.additionalContext = additionalContext;
    }
}

