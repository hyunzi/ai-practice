package com.example.llmping.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevDocsRagService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public DevDocsRagService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    /**
     * Embed the query and retrieve top-k relevant chunks from the embedding store.
     */
    public List<TextSegment> searchTopChunks(String query, String provider, int topK) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(queryEmbedding, topK);

        return matches.stream()
                .map(EmbeddingMatch::embedded)
                .filter(segment -> segment != null)
                .collect(Collectors.toList());
    }
}
