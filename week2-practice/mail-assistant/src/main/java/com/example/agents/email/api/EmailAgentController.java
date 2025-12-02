package com.example.agents.email.api;

import com.example.agents.email.model.EmailChecklistRequest;
import com.example.agents.email.model.EmailChecklistResponse;
import com.example.agents.email.model.EmailDraftRequest;
import com.example.agents.email.model.EmailDraftResponse;
import com.example.agents.email.service.EmailAgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent/email")
public class EmailAgentController {

    private final EmailAgentService emailAgentService;

    public EmailAgentController(EmailAgentService emailAgentService) {
        this.emailAgentService = emailAgentService;
    }

    @PostMapping("/draft")
    public ResponseEntity<EmailDraftResponse> generateDraft(
            @Valid @RequestBody EmailDraftRequest request
    ) {
        EmailDraftResponse response = emailAgentService.generateDraft(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/checklist")
    public ResponseEntity<EmailChecklistResponse> checkDraft(
            @Valid @RequestBody EmailChecklistRequest request
    ) {
        EmailChecklistResponse response = emailAgentService.checkDraft(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

