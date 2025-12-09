package com.example.llmping.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChromaConfig {

    @Bean
    public ChromaStoreFactory chromaStoreFactory(
            @Value("${chroma.enabled:true}") boolean chromaEnabled,
            @Value("${chroma.base-url:http://localhost:8000}") String baseUrl) {
        return new ChromaStoreFactory(chromaEnabled, baseUrl);
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(
            ChromaStoreFactory chromaStoreFactory,
            @Value("${chroma.collection:devdocs-stripe}") String collection) {
        return chromaStoreFactory.create(collection);
    }
}
