package com.example.agents.rag.devdoc;

import com.example.agents.rag.common.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Skeleton implementation for dev-doc RAG.
 *
 * TODO: implement RAG using local dev docs under {@code data/dev-docs}.
 */
@Service("devDocRagService")
public class DevDocRagServiceImpl implements RagService {

    private static final Logger log = LoggerFactory.getLogger(DevDocRagServiceImpl.class);

    @Override
    public List<String> retrieveRelevantTexts(String query, int topK) {
        log.debug("DevDocRagServiceImpl.retrieveRelevantTexts called with query='{}', topK={}", query, topK);

        // TODO: replace this dummy implementation with an EmbeddingStore-based RAG
        // that indexes local dev documentation from data/dev-docs.
        return Collections.singletonList("TODO: implement RAG using local dev docs");
    }
}
