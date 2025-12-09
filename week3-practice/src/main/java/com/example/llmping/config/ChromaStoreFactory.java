package com.example.llmping.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creates Chroma embedding stores per collection, with a fallback to in-memory.
 */
public class ChromaStoreFactory {

    private static final Logger log = LoggerFactory.getLogger(ChromaStoreFactory.class);

    private final boolean chromaEnabled;
    private final String baseUrl;
    private final Map<String, EmbeddingStore<TextSegment>> cache = new ConcurrentHashMap<>();

    public ChromaStoreFactory(boolean chromaEnabled, String baseUrl) {
        this.chromaEnabled = chromaEnabled;
        this.baseUrl = baseUrl;
    }

    public EmbeddingStore<TextSegment> create(String collectionName) {
        return cache.computeIfAbsent(collectionName, name -> {
            if (!chromaEnabled) {
                log.warn("Chroma disabled by configuration; using in-memory embedding store for collection {}", name);
                return new InMemoryEmbeddingStore<>();
            }
            try {
                return ChromaEmbeddingStore.builder()
                        .baseUrl(baseUrl)
                        .collectionName(name)
                        .build();
            } catch (Exception e) {
                log.warn("Chroma connection failed for collection {} ({}). Falling back to in-memory.", name, e.getMessage());
                return new InMemoryEmbeddingStore<>();
            }
        });
    }
}
