package com.example.agents.devdoc.api;

import com.example.agents.devdoc.model.DevDocAnswerResponse;
import com.example.agents.devdoc.model.DevDocNoteRequest;
import com.example.agents.devdoc.model.DevDocNoteResponse;
import com.example.agents.devdoc.model.DevDocQuestionRequest;
import com.example.agents.devdoc.service.DevDocAgentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent/devdoc")
public class DevDocAgentController {

    private static final Logger log = LoggerFactory.getLogger(DevDocAgentController.class);

    private final DevDocAgentService devDocAgentService;

    public DevDocAgentController(DevDocAgentService devDocAgentService) {
        this.devDocAgentService = devDocAgentService;
    }

    @PostMapping("/qa")
    public ResponseEntity<DevDocAnswerResponse> answerQuestion(
            @Valid @RequestBody DevDocQuestionRequest request
    ) {
        log.debug("Received DevDoc QA request: question='{}', persona='{}', topicHint='{}'",
                request.getQuestion(), request.getPersona(), request.getTopicHint());
        DevDocAnswerResponse response = devDocAgentService.answerQuestion(request);
        log.debug("DevDoc QA response ready: summaryLength={}, detailedLength={}",
                response.getSummary() != null ? response.getSummary().length() : 0,
                response.getDetailedAnswer() != null ? response.getDetailedAnswer().length() : 0);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/note")
    public ResponseEntity<DevDocNoteResponse> generateNote(
            @Valid @RequestBody DevDocNoteRequest request
    ) {
        DevDocNoteResponse response = devDocAgentService.generateStudyNote(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
