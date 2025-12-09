package com.example.llmping.service;

import com.example.llmping.model.RagDebugInfo;
import com.example.llmping.model.RetrievedChunkInfo;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevDocsRagService {

    private static final Logger log = LoggerFactory.getLogger(DevDocsRagService.class);

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
        return searchTopChunks(query, provider, topK, null);
    }

    /**
     * Embed the query, retrieve top-k chunks, and log debug info including the final prompt.
     */
    public List<TextSegment> searchTopChunks(String query, String provider, int topK, String finalPrompt) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.findRelevant(queryEmbedding, topK);

        List<RetrievedChunkInfo> chunkInfos = matches.stream()
                .map(this::toChunkInfo)
                .toList();
        RagDebugInfo debugInfo = new RagDebugInfo(finalPrompt, query, provider, topK, chunkInfos);
        logDebug(debugInfo);

        return matches.stream()
                .map(EmbeddingMatch::embedded)
                .filter(segment -> segment != null)
                .collect(Collectors.toList());
    }

    private RetrievedChunkInfo toChunkInfo(EmbeddingMatch<TextSegment> match) {
        TextSegment segment = match.embedded();
        String text = segment != null ? segment.text() : "";
        String preview = text.length() > 200 ? text.substring(0, 200) : text;

        var metadata = segment != null ? segment.metadata() : null;
        String provider = metadata != null ? metadata.getString("provider") : null;
        String section = metadata != null ? metadata.getString("section") : null;
        String fileName = metadata != null ? metadata.getString("fileName") : null;
        String chunkIndex = metadata != null ? metadata.getString("chunkIndex") : null;

        Double score = match == null ? null : Double.valueOf(match.score());
        String chunkId = buildChunkId(provider, section, fileName, chunkIndex);

        return new RetrievedChunkInfo(
                chunkId,
                preview,
                score,
                provider,
                section,
                fileName,
                chunkIndex
        );
    }

    private String buildChunkId(String provider, String section, String fileName, String chunkIndex) {
        StringBuilder sb = new StringBuilder();
        if (provider != null) {
            sb.append(provider);
        }
        if (section != null) {
            if (sb.length() > 0) sb.append("/");
            sb.append(section);
        }
        if (fileName != null) {
            if (sb.length() > 0) sb.append("/");
            sb.append(fileName);
        }
        if (chunkIndex != null) {
            if (sb.length() > 0) sb.append("#");
            sb.append(chunkIndex);
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    private void logDebug(RagDebugInfo debugInfo) {
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug("rag_debug={}", toJson(debugInfo));
    }

    private String toJson(RagDebugInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"finalPrompt\":").append(jsonValue(info.finalPrompt()));
        sb.append(",\"query\":").append(jsonValue(info.query()));
        sb.append(",\"provider\":").append(jsonValue(info.provider()));
        sb.append(",\"topK\":").append(info.topK());
        sb.append(",\"chunks\":[");
        List<RetrievedChunkInfo> chunks = info.chunks();
        for (int i = 0; i < chunks.size(); i++) {
            RetrievedChunkInfo c = chunks.get(i);
            sb.append("{\"chunkId\":").append(jsonValue(c.chunkId()));
            sb.append(",\"preview\":").append(jsonValue(c.preview()));
            sb.append(",\"score\":").append(c.score() == null ? "null" : c.score());
            sb.append(",\"provider\":").append(jsonValue(c.provider()));
            sb.append(",\"section\":").append(jsonValue(c.section()));
            sb.append(",\"fileName\":").append(jsonValue(c.fileName()));
            sb.append(",\"chunkIndex\":").append(jsonValue(c.chunkIndex()));
            sb.append("}");
            if (i < chunks.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    private String jsonValue(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + escapeJson(value) + "\"";
    }

    private String escapeJson(String value) {
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
