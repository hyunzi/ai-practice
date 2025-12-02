package com.example.agents.devdoc.service;

import com.example.agents.devdoc.model.DevDocAnswerResponse;
import com.example.agents.devdoc.model.DevDocNoteRequest;
import com.example.agents.devdoc.model.DevDocNoteResponse;
import com.example.agents.devdoc.model.DevDocQuestionRequest;
import com.example.agents.rag.common.RagService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DevDocAgentServiceImpl implements DevDocAgentService {

    private static final Logger log = LoggerFactory.getLogger(DevDocAgentServiceImpl.class);

    private final ChatLanguageModel devDocAgentModel;
    /**
     * Optional RAG service for dev-doc content.
     * Currently not used, but wired so that we can easily
     * enrich prompts with retrieved snippets later.
     */
    private final Optional<RagService> devDocRagService;

    public DevDocAgentServiceImpl(
            @Qualifier("devDocAgentModel") ChatLanguageModel devDocAgentModel,
            @Qualifier("devDocRagService") Optional<RagService> devDocRagService
    ) {
        this.devDocAgentModel = devDocAgentModel;
        this.devDocRagService = devDocRagService;
    }

    @Override
    public DevDocAnswerResponse answerQuestion(DevDocQuestionRequest request) {
        String persona = request.getPersona();
        String topicHint = request.getTopicHint();

        log.info("DevDoc QA started: question='{}', persona='{}', topicHint='{}'",
                request.getQuestion(), persona, topicHint);

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a senior backend engineer who explains things to another experienced Java backend developer in a practical, production-oriented way.\n");
        prompt.append("Avoid overly theoretical or beginner-level explanations. Focus on realistic code examples, operations, and trade-offs.\n\n");

        if (persona != null && !persona.isBlank()) {
            prompt.append("Persona of the reader (in Korean):\n");
            prompt.append(persona).append("\n\n");
        }

        if (topicHint != null && !topicHint.isBlank()) {
            prompt.append("Topic hint (keywords):\n");
            prompt.append(topicHint).append("\n\n");
        }

        prompt.append("Question:\n");
        prompt.append(request.getQuestion()).append("\n\n");

        prompt.append("Answer in Korean for explanations, but you can include code and important terms in English.\n");
        prompt.append("Respond in the following Markdown format exactly:\n");
        prompt.append("### Summary\n");
        prompt.append("- 3~5 lines summarizing the key points.\n\n");
        prompt.append("### Detailed Answer\n");
        prompt.append("- Use bullet points and short paragraphs.\n");
        prompt.append("- Include concrete examples, typical pitfalls, and ops considerations.\n\n");
        prompt.append("### Referenced Snippets\n");
        prompt.append("- 2~3 key sentences (in English) that capture the most important ideas or patterns.\n");

        // Example spot where RAG results could be injected in the future:
        // List<String> context = devDocRagService
        //         .map(service -> service.retrieveRelevantTexts(request.getQuestion(), 5))
        //         .orElse(List.of());
        // if (!context.isEmpty()) {
        //     prompt.append("\nYou can optionally ground your answer on the following internal dev docs:\n");
        //     context.forEach(snippet -> prompt.append("- ").append(snippet).append("\n"));
        //     prompt.append("\n");
        // }

        String promptText = prompt.toString();
        log.debug("DevDoc QA prompt length={}", promptText.length());

        String rawAnswer = devDocAgentModel.generate(promptText);
        log.debug("DevDoc QA raw answer length={}", rawAnswer != null ? rawAnswer.length() : 0);

        String summarySection = extractSection(rawAnswer, "Summary");
        String detailedSection = extractSection(rawAnswer, "Detailed Answer");
        String snippetsSection = extractSection(rawAnswer, "Referenced Snippets");

        if (detailedSection.isBlank()) {
            detailedSection = rawAnswer.trim();
        }

        if (summarySection.isBlank()) {
            summarySection = "요약 파싱에 실패했습니다. 아래 상세 답변을 참고하세요.";
        }

        List<String> snippets = parseBulletLines(snippetsSection);
        if (snippets.isEmpty()) {
            snippets = Collections.singletonList("중요 문장 추출에 실패했습니다. 상세 답변에서 직접 발췌해 주세요.");
        }

        return new DevDocAnswerResponse(summarySection.trim(), detailedSection.trim(), snippets);
    }

    @Override
    public DevDocNoteResponse generateStudyNote(DevDocNoteRequest request) {
        String topic = request.getTopic();
        String persona = request.getPersona();

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are creating a study note for a developer who is learning a specific backend topic.\n");
        prompt.append("Write the note in Markdown, mixing Korean explanations with English terms and code where appropriate.\n\n");

        prompt.append("Topic:\n");
        prompt.append(topic).append("\n\n");

        if (persona != null && !persona.isBlank()) {
            prompt.append("Persona (in Korean):\n");
            prompt.append(persona).append("\n\n");
        }

        prompt.append("Format the note exactly with the following sections:\n");
        prompt.append("# 제목\n");
        prompt.append("## 요약\n");
        prompt.append("## 핵심 개념\n");
        prompt.append("## 실무 적용 포인트\n");
        prompt.append("## TODO / Follow-up\n\n");
        prompt.append("Fill in each section with appropriate content for the given topic and persona.\n");

        // Example spot where RAG results could be injected in the future:
        // List<String> context = devDocRagService
        //         .map(service -> service.retrieveRelevantTexts(topic, 5))
        //         .orElse(List.of());
        // if (!context.isEmpty()) {
        //     prompt.append("\nYou may reference the following internal docs while writing the note:\n");
        //     context.forEach(snippet -> prompt.append("- ").append(snippet).append("\n"));
        //     prompt.append("\n");
        // }

        String markdownNote = devDocAgentModel.generate(prompt.toString());

        return new DevDocNoteResponse(markdownNote);
    }

    private static String extractSection(String text, String header) {
        String marker = "### " + header;
        int startIndex = text.indexOf(marker);
        if (startIndex < 0) {
            return "";
        }
        startIndex += marker.length();

        int nextHeaderIndex = text.indexOf("### ", startIndex);
        String section;
        if (nextHeaderIndex >= 0) {
            section = text.substring(startIndex, nextHeaderIndex);
        } else {
            section = text.substring(startIndex);
        }
        return section.trim();
    }

    private static List<String> parseBulletLines(String section) {
        if (section == null || section.isBlank()) {
            return Collections.emptyList();
        }
        String[] lines = section.split("\\r?\\n");
        List<String> bullets = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("-")) {
                String content = trimmed.substring(1).trim();
                if (!content.isBlank()) {
                    bullets.add(content);
                }
            }
        }
        return bullets;
    }
}
