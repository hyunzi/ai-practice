package com.example.llmping.controller;

import com.example.llmping.model.EmbeddingDocument;
import com.example.llmping.service.DevDocsIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devdocs")
public class DevDocsController {

    private final DevDocsIngestionService ingestionService;

    public DevDocsController(DevDocsIngestionService ingestionService) {
        this.ingestionService = ingestionService;
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

    public record IngestionResponse(int totalChunks,
                                    List<Map<String, String>> samples,
                                    String error) {
        public IngestionResponse(int totalChunks, List<Map<String, String>> samples) {
            this(totalChunks, samples, null);
        }
    }
}
