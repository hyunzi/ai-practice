package com.example.agents.email.service;

import com.example.agents.email.model.EmailChecklistRequest;
import com.example.agents.email.model.EmailChecklistResponse;
import com.example.agents.email.model.EmailDraftRequest;
import com.example.agents.email.model.EmailDraftResponse;

public interface EmailAgentService {

    EmailDraftResponse generateDraft(EmailDraftRequest request);

    EmailChecklistResponse checkDraft(EmailChecklistRequest request);
}

