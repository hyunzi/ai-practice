package com.example.devdocs.tool;

import com.example.devdocs.service.DevDocsRagService;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DevDocsTools {

    private final DevDocsRagService ragService;

    public DevDocsTools(DevDocsRagService ragService) {
        this.ragService = ragService;
    }

    @Tool("Search devdocs with a user query, provider, and optional mode(size300|size600), then return top 3 chunks concatenated.")
    public String searchDevDocs(String query, String provider, String mode) {
        List<TextSegment> top = ragService.searchTopChunks(query, provider, 3, null, mode);
        if (top.isEmpty()) {
            return "No results found.";
        }
        return top.stream()
                .map(TextSegment::text)
                .collect(Collectors.joining("\n---\n"));
    }

    @Tool("Format an answer summary as an email draft for a given recipient role and tone.")
    public String formatAnswerAsEmail(String answerSummary, String recipientRole, String tone) {
        return """
                Subject: Follow-up for %s

                Tone: %s
                Recipient role: %s

                Summary:
                %s
                """.formatted(recipientRole, tone, recipientRole, answerSummary);
    }
}
