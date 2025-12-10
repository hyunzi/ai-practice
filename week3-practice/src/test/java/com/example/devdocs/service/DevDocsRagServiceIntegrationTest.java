package com.example.devdocs.service;

import dev.langchain4j.data.segment.TextSegment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class DevDocsRagServiceIntegrationTest {

    @Autowired
    DevDocsRagService ragService;

    @Test
    void q1_webhook_retry_policy() {
        printAnswer("How does Stripe recommend setting webhook retry intervals?");
    }

    @Test
    void q2_payment_fail_error_codes() {
        printAnswer("What error codes does Stripe return for failed payments, and how should we handle them?");
    }

    @Test
    void q3_signature_verification() {
        printAnswer("Explain the signature verification process for Stripe webhook events.");
    }

    private void printAnswer(String query) {
        // finalPrompt can be any string that matches the last prompt sent to LLM; here we pass query for visibility.
        List<TextSegment> chunks = ragService.searchTopChunks(query, "stripe", 3, query, null);
        String answer = chunks.stream()
                .map(TextSegment::text)
                .collect(Collectors.joining("\n---\n"));
        System.out.println("Q: " + query);
        System.out.println("Answer:\n" + answer);
    }
}
