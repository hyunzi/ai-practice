package com.example.agents.rag.email;

import com.example.agents.rag.common.RagService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * In-memory RAG service for email examples.
 *
 * Documents can be indexed via {@link #indexTexts(List)} and queried with {@link #retrieveRelevantTexts(String, int)}.
 */
@Service("emailRagService")
public class EmailRagService implements RagService {

    private static final Logger log = LoggerFactory.getLogger(EmailRagService.class);

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    public EmailRagService(EmbeddingModel embeddingModel) {
        this(embeddingModel, new InMemoryEmbeddingStore<>());
    }

    EmailRagService(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = Objects.requireNonNull(embeddingModel, "embeddingModel");
        this.embeddingStore = Objects.requireNonNull(embeddingStore, "embeddingStore");
    }

    /**
     * Index plain-text examples into the embedding store.
     */
    public void indexTexts(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return;
        }

        List<TextSegment> segments = texts.stream()
                .filter(Objects::nonNull)
                .map(TextSegment::from)
                .toList();

        if (segments.isEmpty()) {
            return;
        }

        Response<List<Embedding>> response = embeddingModel.embedAll(segments);
        List<Embedding> embeddings = response.content();
        if (embeddings == null || embeddings.isEmpty()) {
            log.warn("No embeddings produced while indexing {} texts", segments.size());
            return;
        }

        embeddingStore.addAll(embeddings, segments);
    }

    @Override
    public List<String> retrieveRelevantTexts(String query, int topK) {
        if (query == null || query.isBlank() || topK <= 0) {
            return Collections.emptyList();
        }

        Response<Embedding> response = embeddingModel.embed(query);
        Embedding queryEmbedding = response.content();
        if (queryEmbedding == null) {
            return Collections.emptyList();
        }

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(searchRequest);
        List<EmbeddingMatch<TextSegment>> matches = result == null ? Collections.emptyList() : result.matches();

        return matches.stream()
                .map(EmbeddingMatch::embedded)
                .map(TextSegment::text)
                .toList();
    }
}
