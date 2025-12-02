package com.example.agents.rag.email;

import com.example.agents.rag.common.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Skeleton implementation for email RAG.
 *
 * TODO: implement RAG using local email examples under {@code data/email-examples}.
 */
@Service("emailRagService")
public class EmailRagServiceImpl implements RagService {

    private static final Logger log = LoggerFactory.getLogger(EmailRagServiceImpl.class);

    @Override
    public List<String> retrieveRelevantTexts(String query, int topK) {
        log.debug("EmailRagServiceImpl.retrieveRelevantTexts called with query='{}', topK={}", query, topK);

        // TODO: replace this dummy implementation with an EmbeddingStore-based RAG
        // that indexes example emails from data/email-examples.
        return Collections.singletonList("TODO: implement RAG using local email examples");
    }
}
