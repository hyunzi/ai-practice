package com.example.llmping.service;

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
        printAnswer("Stripe webhook 재시도 횟수와 간격 알려줘");
    }

    @Test
    void q2_payment_fail_error_codes() {
        printAnswer("결제 실패 시 Stripe가 에러 코드를 어떻게 반환하는지 알고 싶어");
    }

    @Test
    void q3_signature_verification() {
        printAnswer("결제 이벤트 시그니처 검증 방법 설명해줘");
    }

    private void printAnswer(String query) {
        // finalPrompt can be any string that matches the last prompt sent to LLM; here we pass query for visibility.
        List<TextSegment> chunks = ragService.searchTopChunks(query, "stripe", 3, query);
        String answer = chunks.stream()
                .map(TextSegment::text)
                .collect(Collectors.joining("\n---\n"));
        System.out.println("Q: " + query);
        System.out.println("Answer:\n" + answer);
    }
}
