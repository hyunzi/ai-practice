package com.example.agents.rag.email;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmailRagServiceTest {

    private static class FakeEmbeddingModel implements EmbeddingModel {

        @Override
        public Response<Embedding> embed(String text) {
            // Simple deterministic embedding based on text length
            Embedding embedding = Embedding.from(new float[]{text.length(), 1.0f});
            return Response.from(embedding);
        }

        @Override
        public Response<List<Embedding>> embedAll(List<String> texts) {
            List<Embedding> embeddings = texts.stream()
                    .map(t -> Embedding.from(new float[]{t.length(), 1.0f}))
                    .toList();
            return Response.from(embeddings);
        }
    }

    @Test
    void retrieveRelevantTexts_returnsEmptyList_whenNoDocumentsIndexed() {
        EmbeddingModel embeddingModel = new FakeEmbeddingModel();
        EmailRagService service = new EmailRagService(embeddingModel);

        List<String> results = service.retrieveRelevantTexts("test query", 5);

        assertThat(results).isEmpty();
    }
}
