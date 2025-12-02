package com.example.agents.devdoc.model;

public class DevDocNoteResponse {

    private String markdownNote;

    public DevDocNoteResponse() {
    }

    public DevDocNoteResponse(String markdownNote) {
        this.markdownNote = markdownNote;
    }

    public String getMarkdownNote() {
        return markdownNote;
    }

    public void setMarkdownNote(String markdownNote) {
        this.markdownNote = markdownNote;
    }
}

