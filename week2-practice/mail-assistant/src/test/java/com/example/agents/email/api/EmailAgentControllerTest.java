package com.example.agents.email.api;

import com.example.agents.email.model.EmailDraftRequest;
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
class EmailAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Replace the real emailAgentModel with a mock to keep the
     * integration test fast and independent from a running Ollama instance.
     *
     * We use the same bean name and type as in {@link LlmConfig#emailAgentModel()}.
     */
    @MockBean(name = "emailAgentModel")
    private ChatLanguageModel emailAgentModel;

    @Test
    void draftEndpoint_returnsEnglishMail() throws Exception {
        given(emailAgentModel.generate(anyString()))
                .willReturn("Dear team,\nThis is a mocked English email draft.\nBest regards,\nTester");

        EmailDraftRequest request = new EmailDraftRequest(
                "안녕하세요, 지난주에 논의한 결제 API 변경 건 관련해서 다시 공유드립니다.",
                "formal",
                "NEW",
                "수신자는 외부 파트너사 PM입니다."
        );

        String json = """
                {
                  "koreanDraft": "%s",
                  "tone": "%s",
                  "mailType": "%s",
                  "additionalContext": "%s"
                }
                """.formatted(
                request.getKoreanDraft(),
                request.getTone(),
                request.getMailType(),
                request.getAdditionalContext()
        );

        mockMvc.perform(post("/api/agent/email/draft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalEnglishMail", not(nullValue())))
                .andExpect(jsonPath("$.finalEnglishMail").isString());
    }
}

