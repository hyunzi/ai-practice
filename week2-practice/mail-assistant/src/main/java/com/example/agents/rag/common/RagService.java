package com.example.agents.rag.common;

import java.util.List;

public interface RagService {

    List<String> retrieveRelevantTexts(String query, int topK);
}

