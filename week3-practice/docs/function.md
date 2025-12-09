# 기능별 기술 적용 흐름 (Week 3: DevDocs RAG Assistant)

## 전체 호출 플로우
```
사용자 질문
  → POST /api/agent/ask
  → LangChain4j AiService(DevDocsAssistant)
     → 필요 시 tool.searchDevDocs(query, provider, mode) 호출 → RAG 검색
     → (옵션) tool.formatAnswerAsEmail(summary, role, tone) 호출
  → 최종 답변 또는 이메일 초안
```

## LLM / 프롬프트
- 모델: `gpt-4o-mini`(`OpenAiConfig`)를 `ChatLanguageModel`로 등록.
- 프롬프트: `agent/DevDocsAssistant`에 `@SystemMessage`로 Stripe DevDocs 도우미 역할 지정, `@UserMessage`로 사용자의 질문 전달.
- Raw 호출: `/api/openai/chat`에서 `chatLanguageModel.generate()`로 RAG 없이 직접 호출.

## Function Calling / Tools
- 등록 위치: `config/AgentConfig` → `DevDocsTools` 바인딩.
- 제공 도구
  - `searchDevDocs(query, provider, mode)` : `DevDocsRagService.searchTopChunks(..., topK=3, mode)`를 호출해 상위 청크 텍스트를 합쳐 반환. `mode=size300|size600`에 따라 컬렉션 선택, `provider` 기본값은 백엔드에서 "stripe".
  - `formatAnswerAsEmail(answerSummary, recipientRole, tone)` : 요약을 이메일 초안 형태로 포매팅.
- LLM이 도구 호출 결과를 받아 최종 응답을 생성.

## RAG 파이프라인
- 검색 서비스: `service/DevDocsRagService`
  - 쿼리 임베딩 → `EmbeddingStore.findRelevant(topK)` → 상위 청크 반환.
  - 모드별 컬렉션: `size300` → `devdocs-stripe-300`, `size600` → `devdocs-stripe-600`(기타는 300 기본).
  - 디버그 로그: `rag_debug=...` 형태로 프롬프트/쿼리/매치 프리뷰 기록.
- 인젝션 서비스: `service/DevDocsIngestionService`
  - 입력: `data/devdocs/stripe/**/*.md`
  - 분할: `DocumentSplitters.recursive(chunkSize, 80, OpenAiTokenizer("gpt-4o-mini"))`
  - 메타데이터: `provider`, `section`, `fileName`, `chunkIndex`
  - 저장: `ChromaStoreFactory`를 통해 Chroma(기본) 또는 인메모리 스토어에 저장.

## 엔드포인트별 행동
- `POST /api/devdocs/ingest` : 기본 300 사이즈 컬렉션에 인젝션, 샘플 5개 포함 응답.
- `POST /api/devdocs/query` : `{ question, provider, mode }` → RAG top-3 청크 반환.
- `POST /api/agent/ask` : `{ question, mode }` → 에이전트 응답(툴 호출 가능).
- `POST /api/openai/chat` : `{ message }` → 모델 직접 호출.
- `GET /api/devdocs/collections` / `GET /api/devdocs/collections/{name}/preview` : Chroma 컬렉션 조회 및 샘플 확인.
