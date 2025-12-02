package com.example.agents.email.model;

import java.util.List;

public class EmailChecklistResponse {

    private List<String> missingItems;
    private List<String> suggestions;

    public EmailChecklistResponse() {
    }

    public EmailChecklistResponse(List<String> missingItems, List<String> suggestions) {
        this.missingItems = missingItems;
        this.suggestions = suggestions;
    }

    public List<String> getMissingItems() {
        return missingItems;
    }

    public void setMissingItems(List<String> missingItems) {
        this.missingItems = missingItems;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}

