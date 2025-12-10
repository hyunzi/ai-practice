package com.example.devdocs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Lightweight helper to inspect Chroma collections and sample documents via the HTTP API.
 */
@Service
public class ChromaIntrospectionService {

    private static final Logger log = LoggerFactory.getLogger(ChromaIntrospectionService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public ChromaIntrospectionService(@Value("${chroma.base-url:http://localhost:8000}") String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public List<String> listCollections() {
        try {
            var response = restTemplate.getForObject(baseUrl + "/api/v1/collections", Map.class);
            if (response == null || !response.containsKey("collections")) {
                return Collections.emptyList();
            }
            var collections = (List<Map<String, Object>>) response.get("collections");
            return collections.stream()
                    .map(c -> (String) c.get("name"))
                    .toList();
        } catch (Exception e) {
            log.warn("Failed to list Chroma collections: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ChromaPreviewItem> preview(String collectionName, int limit) {
        try {
            String collectionId = resolveCollectionId(collectionName);
            if (collectionId == null) {
                return Collections.emptyList();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = Map.of(
                    "limit", limit,
                    "include", List.of("documents", "metadatas")
            );
            var entity = new HttpEntity<>(body, headers);
            var response = restTemplate.postForObject(
                    baseUrl + "/api/v1/collections/" + collectionId + "/get",
                    entity,
                    Map.class);
            if (response == null) {
                return Collections.emptyList();
            }
            List<String> documents = (List<String>) response.getOrDefault("documents", Collections.emptyList());
            List<Map<String, Object>> metadatas = (List<Map<String, Object>>) response.getOrDefault("metadatas", Collections.emptyList());
            int size = Math.min(documents.size(), metadatas.size());
            new StringBuilder(); // avoid unused warning on SB imports
            return java.util.stream.IntStream.range(0, size)
                    .mapToObj(i -> new ChromaPreviewItem(
                            documents.get(i),
                            metadatas.get(i)
                    ))
                    .toList();
        } catch (Exception e) {
            log.warn("Failed to preview Chroma collection {}: {}", collectionName, e.getMessage());
            return Collections.emptyList();
        }
    }

    private String resolveCollectionId(String name) {
        try {
            var response = restTemplate.getForObject(baseUrl + "/api/v1/collections/" + name, Map.class);
            if (response != null && response.containsKey("id")) {
                return (String) response.get("id");
            }
        } catch (Exception e) {
            log.warn("Failed to resolve Chroma collection {}: {}", name, e.getMessage());
        }
        return null;
    }

    public record ChromaPreviewItem(String document, Map<String, Object> metadata) {}
}
