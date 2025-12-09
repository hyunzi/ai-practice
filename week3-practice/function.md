# 기능별 기술 적용 정리 (Week 3: DevDocs RAG Assistant)

## 전체 워크플로 개요
```
사용자 질문
   ↓
POST /api/agent/ask
   ↓
LangChain4j AiService(DevDocsAssistant)
   ↓ (필요 시 함수 호출)
   ├─ searchDevDocs → RAG 검색 (Chroma/메모리)
   └─ formatAnswerAsEmail → 이메일 초안 포맷
   ↓
최종 답변/이메일 초안
```

## LLM 호출
- 구성: `OpenAiConfig`에서 `gpt-4o-mini`를 `ChatLanguageModel`로 빈 등록.
- 직접 호출: `OpenAiChatController` → `chatLanguageModel.generate(message)`.
- 에이전트 호출: AiService(프록시) 내부에서 질문/툴 결과를 바탕으로 LLM이 최종 답변 생성.

## Prompt Chain
- `DevDocsAssistant` 인터페이스에 `@SystemMessage`로 역할/톤 정의, `@UserMessage`로 사용자 질문 전달.
- 체인 흐름: 시스템 프롬프트 → 사용자 질문 → (툴 호출 결과 포함) → LLM 최종 응답.

## Function Calling / Tool Use
- 설정: `AgentConfig`에서 AiService에 `DevDocsTools` 등록 → LangChain4j가 자동 함수 호출 활성화.
- 툴:
  - `searchDevDocs(query, provider)`: RAG 검색 Top 3 청크를 묶어 컨텍스트로 반환.
  - `formatAnswerAsEmail(answerSummary, recipientRole, tone)`: 답변 요약을 이메일 초안 형태로 변환.
- LLM이 필요성을 판단해 함수 호출 → 결과를 받아 최종 답변에 반영.

## Workflow (요청~응답 경로)
1) 클라이언트가 `/api/agent/ask`로 질문을 전달.
2) AiService가 시스템/사용자 프롬프트를 구성하고 LLM 호출.
3) LLM이 추가 컨텍스트 필요 시 `searchDevDocs` 호출 → RAG 검색 결과 수신.
4) 필요한 경우 `formatAnswerAsEmail`로 이메일 초안 생성.
5) LLM이 최종 답변/초안을 조립해 반환.

## RAG: 검색 증강 생성
- 인덱싱: `DevDocsIngestionService`가 DevDocs MD를 읽어 청킹 후 임베딩을 생성, `EmbeddingStore`에 저장.
- 질의: `DevDocsRagService.searchTopChunks`가 쿼리를 임베딩하고 `findRelevant(topK)`로 유사 청크 반환.
- 활용: `searchDevDocs` 툴이 상위 청크를 LLM 컨텍스트로 제공.

## Chunking / Embedding
- 청킹: `DocumentSplitters.recursive(300, 80, OpenAiTokenizer)`로 약 300토큰, 80토큰 오버랩.
- 임베딩: `OpenAiEmbeddingModel`(`text-embedding-3-small`)로 세그먼트 임베딩 생성.
- 저장: 기본 `ChromaEmbeddingStore`, 실패 시 `InMemoryEmbeddingStore`; 메타데이터(`provider`, `section`, `fileName`, `chunkIndex`) 포함.

## 주요 클래스/파일 매핑
- LLM/Embedding 설정: `src/main/java/com/example/llmping/config/OpenAiConfig.java`
- 벡터 스토어 설정: `config/ChromaConfig.java`
- 에이전트/툴 설정: `config/AgentConfig.java`, `agent/DevDocsAssistant.java`, `tool/DevDocsTools.java`
- RAG 인덱싱/검색: `service/DevDocsIngestionService.java`, `service/DevDocsRagService.java`
- 엔드포인트: `controller/DevDocsController.java`, `controller/AgentController.java`, `controller/OpenAiChatController.java`
