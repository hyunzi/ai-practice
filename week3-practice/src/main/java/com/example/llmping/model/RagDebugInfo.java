package com.example.llmping.model;

import java.util.List;

/**
 * Captures the final prompt and retrieved chunks for RAG debug logging.
 */
public record RagDebugInfo(
        String finalPrompt,
        String query,
        String provider,
        int topK,
        List<RetrievedChunkInfo> chunks
) {
}
