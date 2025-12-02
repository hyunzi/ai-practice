package com.example.agents.email.model;

public class EmailDraftResponse {

    private String finalEnglishMail;
    private String detectedMailType;
    private String appliedTone;

    public EmailDraftResponse() {
    }

    public EmailDraftResponse(String finalEnglishMail, String detectedMailType, String appliedTone) {
        this.finalEnglishMail = finalEnglishMail;
        this.detectedMailType = detectedMailType;
        this.appliedTone = appliedTone;
    }

    public String getFinalEnglishMail() {
        return finalEnglishMail;
    }

    public void setFinalEnglishMail(String finalEnglishMail) {
        this.finalEnglishMail = finalEnglishMail;
    }

    public String getDetectedMailType() {
        return detectedMailType;
    }

    public void setDetectedMailType(String detectedMailType) {
        this.detectedMailType = detectedMailType;
    }

    public String getAppliedTone() {
        return appliedTone;
    }

    public void setAppliedTone(String appliedTone) {
        this.appliedTone = appliedTone;
    }
}

