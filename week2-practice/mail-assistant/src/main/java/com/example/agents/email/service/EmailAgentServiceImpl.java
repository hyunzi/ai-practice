package com.example.agents.email.service;

import com.example.agents.email.model.EmailChecklistRequest;
import com.example.agents.email.model.EmailChecklistResponse;
import com.example.agents.email.model.EmailDraftRequest;
import com.example.agents.email.model.EmailDraftResponse;
import com.example.agents.rag.common.RagService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EmailAgentServiceImpl implements EmailAgentService {

    private final ChatLanguageModel emailAgentModel;
    /**
     * Optional RAG service for email examples.
     * Currently not used, but wired so that we can easily
     * inject retrieved texts into prompts later.
     */
    private final Optional<RagService> emailRagService;

    public EmailAgentServiceImpl(
            @Qualifier("emailAgentModel") ChatLanguageModel emailAgentModel,
            @Qualifier("emailRagService") Optional<RagService> emailRagService
    ) {
        this.emailAgentModel = emailAgentModel;
        this.emailRagService = emailRagService;
    }

    @Override
    public EmailDraftResponse generateDraft(EmailDraftRequest request) {
        String tone = defaultIfBlank(request.getTone(), "soft");
        String mailType = defaultIfBlank(request.getMailType(), "AUTO").toUpperCase(Locale.ROOT);

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an assistant that helps a Korean backend developer write polite, natural-sounding English business emails.\n");
        prompt.append("User will provide a Korean draft, tone preference, mail type, and additional context.\n");
        prompt.append("Write a complete English email ready to send.\n\n");

        prompt.append("Instructions:\n");
        prompt.append("- Preserve the intent and key information from the Korean draft.\n");
        prompt.append("- Tone: ").append(tone).append(". Adjust formality and phrasing accordingly.\n");
        prompt.append("- Mail type: ").append(mailType).append(". If type is AUTO, first infer the best mail type.\n");
        prompt.append("- Use clear paragraphs and natural business email style.\n");
        prompt.append("- Include appropriate greeting and closing.\n\n");

        if (!"AUTO".equals(mailType)) {
            prompt.append("Mail structure hints for type ").append(mailType).append(":\n");
            switch (mailType) {
                case "NEW" -> prompt.append("- Brief introduction, purpose, main points, and clear call to action.\n");
                case "REPLY" -> prompt.append("- Reference the previous email, address questions, and clarify next steps.\n");
                case "APOLOGY" -> prompt.append("- Clearly apologize, acknowledge the impact, and describe follow-up actions.\n");
                case "REMINDER" -> prompt.append("- Politely remind, mention previous communication, and restate the request or deadline.\n");
                default -> {
                }
            }
            prompt.append("\n");
        }

        prompt.append("Korean draft:\n");
        prompt.append(request.getKoreanDraft()).append("\n\n");

        if (request.getAdditionalContext() != null && !request.getAdditionalContext().isBlank()) {
            prompt.append("Additional context:\n");
            prompt.append(request.getAdditionalContext()).append("\n\n");
        }

        // Example spot where RAG results could be injected in the future:
        // List<String> examples = emailRagService
        //         .map(service -> service.retrieveRelevantTexts(request.getKoreanDraft(), 5))
        //         .orElse(List.of());
        // if (!examples.isEmpty()) {
        //     prompt.append("You may refer to the following internal email examples as additional context:\n");
        //     examples.forEach(example -> prompt.append("- ").append(example).append("\n"));
        //     prompt.append("\n");
        // }

        prompt.append("Now write the final English email only, without any explanations.\n");

        String finalEnglishMail = emailAgentModel.generate(prompt.toString());

        String detectedMailType = mailType;
        // For now, if AUTO, we simply keep AUTO as detected type.
        // Later, we can refine by asking the LLM to classify.

        return new EmailDraftResponse(finalEnglishMail, detectedMailType, tone);
    }

    @Override
    public EmailChecklistResponse checkDraft(EmailChecklistRequest request) {
        String englishMail = request.getEnglishMail();

        List<String> checklistItems = List.of(
                "수신자 또는 참조(TO/CC 대상)가 명확하게 언급되었는가?",
                "메일을 보내는 이유/배경이 분명하게 설명되었는가?",
                "요청사항 또는 next action이 명확하게 적혀 있는가?",
                "데드라인 또는 일정이 필요한 경우, 이를 언급했는가?",
                "마무리 인사와 서명이 적절한가?"
        );

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an assistant that reviews an English business email.\n");
        prompt.append("Check the email according to the following checklist (written in Korean):\n");
        for (String item : checklistItems) {
            prompt.append("- ").append(item).append("\n");
        }
        prompt.append("\n");
        prompt.append("Email to review:\n");
        prompt.append(englishMail).append("\n\n");
        prompt.append("Task:\n");
        prompt.append("- Identify which checklist items are missing or weakly addressed.\n");
        prompt.append("- Provide concrete suggestions in bullet points to improve the email.\n");
        prompt.append("- Respond in Korean, but keep example sentences in English where helpful.\n");

        // Example spot where RAG results could be injected in the future:
        // List<String> examples = emailRagService
        //         .map(service -> service.retrieveRelevantTexts(englishMail, 5))
        //         .orElse(List.of());
        // if (!examples.isEmpty()) {
        //     prompt.append("\nYou may also consider the following internal email patterns:\n");
        //     examples.forEach(example -> prompt.append("- ").append(example).append("\n"));
        // }

        String suggestionsText = emailAgentModel.generate(prompt.toString());

        // 초기 버전: LLM 결과 전체를 suggestions에 넣고,
        // missingItems는 요약적인 안내 문구만 넣는다.
        List<String> missingItems = List.of(
                "상세한 누락/보완 항목은 suggestions 내용을 참고하세요."
        );

        List<String> suggestions = List.of(suggestionsText);

        return new EmailChecklistResponse(missingItems, suggestions);
    }

    private static String defaultIfBlank(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}

