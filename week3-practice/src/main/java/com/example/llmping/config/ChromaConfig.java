package com.example.llmping.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromaConfig {

    private static final Logger log = LoggerFactory.getLogger(ChromaConfig.class);

    @Bean
    @ConditionalOnProperty(name = "chroma.enabled", havingValue = "true", matchIfMissing = true)
    public EmbeddingStore<TextSegment> chromaEmbeddingStore(
            @Value("${chroma.base-url:http://localhost:8000}") String baseUrl,
            @Value("${chroma.collection:devdocs-stripe}") String collection) {
        try {
            return ChromaEmbeddingStore.builder()
                    .baseUrl(baseUrl)
                    .collectionName(collection)
                    .build();
        } catch (Exception e) {
            log.warn("Chroma connection failed ({}). Falling back to in-memory embedding store.", e.getMessage());
            return new InMemoryEmbeddingStore<>();
        }
    }

    @Bean
    @ConditionalOnProperty(name = "chroma.enabled", havingValue = "false")
    public EmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}
