package com.example.agents.devdoc.service;

import com.example.agents.devdoc.model.DevDocAnswerResponse;
import com.example.agents.devdoc.model.DevDocNoteRequest;
import com.example.agents.devdoc.model.DevDocNoteResponse;
import com.example.agents.devdoc.model.DevDocQuestionRequest;

public interface DevDocAgentService {

    DevDocAnswerResponse answerQuestion(DevDocQuestionRequest request);

    DevDocNoteResponse generateStudyNote(DevDocNoteRequest request);
}

