package com.example.llmping.model;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;

/**
 * Simple in-memory representation of a chunk with its embedding and metadata.
 */
public record EmbeddingDocument(TextSegment segment, Embedding embedding, Metadata metadata) {
}
