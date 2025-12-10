package com.example.devdocs.controller;

import com.example.devdocs.model.EmbeddingDocument;
import com.example.devdocs.service.DevDocsIngestionService;
import com.example.devdocs.service.DevDocsRagService;
import com.example.devdocs.service.ChromaIntrospectionService;
import com.example.devdocs.service.ChromaIntrospectionService.ChromaPreviewItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devdocs")
public class DevDocsController {

    private final DevDocsIngestionService ingestionService;
    private final DevDocsRagService ragService;
    private final ChromaIntrospectionService chromaIntrospectionService;

    public DevDocsController(DevDocsIngestionService ingestionService,
                             DevDocsRagService ragService,
                             ChromaIntrospectionService chromaIntrospectionService) {
        this.ingestionService = ingestionService;
        this.ragService = ragService;
        this.chromaIntrospectionService = chromaIntrospectionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<IngestionResponse> ingest() {
        try {
            List<EmbeddingDocument> ingested = ingestionService.ingest();
            return ResponseEntity.ok(new IngestionResponse(
                    ingested.size(),
                    ingested.stream()
                            .limit(5)
                            .map(doc -> Map.of(
                                    "fileName", doc.metadata().getString("fileName"),
                                    "section", doc.metadata().getString("section"),
                                    "provider", doc.metadata().getString("provider"),
                                    "chunkIndex", doc.metadata().getString("chunkIndex"),
                                    "text", doc.segment().text()
                            ))
                            .toList()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new IngestionResponse(0, List.of(), "Failed to ingest devdocs: " + e.getMessage()));
        }
    }

    @PostMapping("/query")
    public ResponseEntity<RagQueryResponse> query(@org.springframework.web.bind.annotation.RequestBody RagQueryRequest request) {
        String question = request.question == null ? "" : request.question;
        String mode = request.mode;
        String provider = request.provider == null || request.provider.isBlank() ? "stripe" : request.provider;
        var chunks = ragService.searchTopChunks(question, provider, 3, null, mode);
        return ResponseEntity.ok(new RagQueryResponse(question, mode, provider, chunks.stream()
                .map(TextSegmentView::from)
                .toList()));
    }

    @GetMapping("/collections")
    public ResponseEntity<CollectionListResponse> listCollections() {
        List<String> collections = chromaIntrospectionService.listCollections();
        return ResponseEntity.ok(new CollectionListResponse(collections));
    }

    @GetMapping("/collections/{name}/preview")
    public ResponseEntity<PreviewResponse> preview(
            @PathVariable("name") String collectionName,
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        List<ChromaPreviewItem> items = chromaIntrospectionService.preview(collectionName, limit);
        return ResponseEntity.ok(new PreviewResponse(collectionName, limit, items));
    }

    public record IngestionResponse(int totalChunks,
                                    List<Map<String, String>> samples,
                                    String error) {
        public IngestionResponse(int totalChunks, List<Map<String, String>> samples) {
            this(totalChunks, samples, null);
        }
    }

    public static class RagQueryRequest {
        public String question;
        public String mode;
        public String provider;
    }

    public record CollectionListResponse(List<String> collections) {}

    public record PreviewResponse(String collection, int limit, List<ChromaPreviewItem> items) {}

    public record RagQueryResponse(String question, String mode, String provider, List<TextSegmentView> chunks) {}

    public record TextSegmentView(String text, Map<String, String> metadata) {
        public static TextSegmentView from(dev.langchain4j.data.segment.TextSegment segment) {
            return new TextSegmentView(segment.text(), segment.metadata().asMap());
        }
    }
}
