package com.example.llmping.service;

import com.example.llmping.model.EmbeddingDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DevDocsIngestionService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final Path devDocsDir;
    private final List<EmbeddingDocument> ingested = new ArrayList<>();

    public DevDocsIngestionService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            @Value("${devdocs.stripe.path:data/devdocs/stripe}") String devDocsPath) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.devDocsDir = Paths.get(devDocsPath);
    }

    /**
     * Reads all markdown files under devdocs directory, chunks them, embeds each chunk,
     * and stores them in an in-memory vector store.
     */
    public List<EmbeddingDocument> ingest() throws IOException {
        ingested.clear();

        if (!Files.exists(devDocsDir)) {
            return Collections.emptyList();
        }

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 80, new OpenAiTokenizer("gpt-4o-mini"));

        Files.walk(devDocsDir)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".md"))
                .forEach(path -> {
            try {
                        processFile(path, splitter);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process file: " + path, e);
                    }
                });

        return List.copyOf(ingested);
    }

    private void processFile(Path path, DocumentSplitter splitter) throws IOException {
        String content = Files.readString(path, StandardCharsets.UTF_8);

        if (content == null || content.isBlank()) {
            return;
        }

        Metadata metadata = Metadata.from(Map.of(
                "provider", "stripe",
                "section", path.getParent() != null ? path.getParent().getFileName().toString() : "",
                "fileName", path.getFileName().toString()
        ));

        Document document = Document.from(content, metadata);
        List<TextSegment> segments = splitter.split(document);

        for (int i = 0; i < segments.size(); i++) {
            TextSegment segment = segments.get(i);
            Metadata segmentMetadata = segment.metadata().copy()
                    .add("chunkIndex", Integer.toString(i));

            TextSegment segmentWithMetadata = TextSegment.from(segment.text(), segmentMetadata);
            Embedding embedding = embeddingModel.embed(segmentWithMetadata).content();

            embeddingStore.add(embedding, segmentWithMetadata);
            ingested.add(new EmbeddingDocument(segmentWithMetadata, embedding, segmentMetadata));
        }
    }

    public List<EmbeddingDocument> getVectorStore() {
        return List.copyOf(ingested);
    }
}
