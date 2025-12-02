package com.example.agents.devdoc.api;

import com.example.agents.llm.LlmConfig;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DevDocAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Replace the real devDocAgentModel with a mock to keep the
     * integration test fast and independent from a running Ollama instance.
     *
     * We use the same bean name and type as in {@link LlmConfig#devDocAgentModel()}.
     */
    @MockBean(name = "devDocAgentModel")
    private ChatLanguageModel devDocAgentModel;

    @Test
    void qaEndpoint_returnsSummaryAndDetailedAnswer() throws Exception {
        String mockedAnswer = """
                ### Summary
                - Webhook 에러는 재시도 전략과 idempotency를 중심으로 처리해야 한다.
                - 장애 전파를 최소화하기 위한 모니터링과 알림이 중요하다.

                ### Detailed Answer
                - 에러 유형을 분류해서 재시도 가능한 경우/불가능한 경우를 나눈다.
                - DLQ, retry queue 등을 사용해 운영 관점에서 안전하게 처리한다.

                ### Referenced Snippets
                - \"Use idempotent endpoints to safely retry failed webhooks.\"
                - \"Monitor failure rates and alert on anomalies.\"
                """;

        given(devDocAgentModel.generate(anyString()))
                .willReturn(mockedAnswer);

        String json = """
                {
                  "question": "이 시스템의 webhook 에러 핸들링 개념을 요약해줘",
                  "persona": "Java 백엔드 10년차 개발자 기준으로 설명해줘",
                  "topicHint": "webhook, error handling"
                }
                """;

        mockMvc.perform(post("/api/agent/devdoc/qa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", not(nullValue())))
                .andExpect(jsonPath("$.detailedAnswer", not(nullValue())));
    }
}

