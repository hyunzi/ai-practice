package com.example.agents.devdoc.model;

import java.util.List;

public class DevDocAnswerResponse {

    private String summary;
    private String detailedAnswer;
    private List<String> referencedSnippets;

    public DevDocAnswerResponse() {
    }

    public DevDocAnswerResponse(String summary, String detailedAnswer, List<String> referencedSnippets) {
        this.summary = summary;
        this.detailedAnswer = detailedAnswer;
        this.referencedSnippets = referencedSnippets;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetailedAnswer() {
        return detailedAnswer;
    }

    public void setDetailedAnswer(String detailedAnswer) {
        this.detailedAnswer = detailedAnswer;
    }

    public List<String> getReferencedSnippets() {
        return referencedSnippets;
    }

    public void setReferencedSnippets(List<String> referencedSnippets) {
        this.referencedSnippets = referencedSnippets;
    }
}

