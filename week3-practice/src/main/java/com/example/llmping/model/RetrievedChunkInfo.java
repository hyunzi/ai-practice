package com.example.llmping.model;

/**
 * Represents a single retrieved chunk with key metadata for debugging.
 */
public record RetrievedChunkInfo(
        String chunkId,
        String preview,
        Double score,
        String provider,
        String section,
        String fileName,
        String chunkIndex
) {
}
