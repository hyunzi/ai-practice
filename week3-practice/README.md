# DevDocs RAG Assistant (Week 3)

Stripe DevDocs를 검색해 답변과 이메일 초안을 만들어 주는 Spring Boot + LangChain4j 예제입니다. OpenAI LLM, RAG, 함수 호출 기반 툴 체인을 사용하며, UI는 `static/devdocs.html`에서 바로 확인할 수 있습니다. 🔍✉️

## 프로젝트 한눈에 👀
- Markdown 소스(`data/devdocs/stripe/**/*.md`)를 청킹·임베딩해 Chroma(또는 메모리) 벡터 스토어에 적재.
- LangChain4j AiService가 에이전트 질문을 받아 LLM을 호출하고, 필요하면 RAG 검색/이메일 포맷팅 툴을 자동 호출.
- RAG 없이 LLM만 호출하는 비교 엔드포인트와 브라우저 테스트 페이지(`/devdocs.html`) 제공.

## 기술 적용 상세 🛠️
- **LLM 호출 🤖**  
  `OpenAiChatController`가 `ChatLanguageModel.generate()`로 직접 응답을 생성합니다. `OpenAiConfig`에서 `gpt-4o-mini`(chat), `text-embedding-3-small`(embedding)을 API 키 기반으로 빈 등록합니다.

- **Prompt chain 🧩**  
  `DevDocsAssistant` 인터페이스에 `@SystemMessage`로 “Stripe DevDocs를 검색해 친절히 답변” 역할을 정의하고, `@UserMessage`로 사용자의 질문을 전달합니다. LangChain4j AiService가 **시스템 프롬프트 → 사용자 질문 → (필요 시) 툴 결과 → 최종 답변** 순서의 체인을 구성합니다.

- **Function calling / Tool use / Workflow 🔗**  
  `AgentConfig`에서 AiService에 `DevDocsTools`를 등록하면 LLM이 자동으로 함수 호출을 결정합니다.  
  - `searchDevDocs(query, provider)`: RAG 검색 결과 상위 3개 청크를 합쳐 컨텍스트로 제공.  
  - `formatAnswerAsEmail(answerSummary, recipientRole, tone)`: 답변 요약을 이메일 초안 형태로 포맷.  
  전체 흐름: **사용자 요청 → `/api/agent/ask` → AiService → 필요 툴 자동 호출 → LLM이 최종 답변/초안 작성**.

- **RAG · Chunking/Embedding 📚**  
  `DevDocsIngestionService`가 `DocumentSplitters.recursive(300, 80, OpenAiTokenizer)`로 약 300토큰 단위, 80토큰 오버랩 청킹을 수행합니다. 각 세그먼트를 `OpenAiEmbeddingModel`로 임베딩 후 `EmbeddingStore`(기본 Chroma, 실패 시 `InMemoryEmbeddingStore`)에 `provider/section/fileName/chunkIndex` 메타데이터와 함께 저장합니다.  
  `DevDocsRagService`는 질의를 임베딩하고 `findRelevant(topK)`로 유사도가 높은 청크를 조회해 에이전트 툴(`searchDevDocs`)에 공급합니다.

## 실행 방법 ▶️
1) OpenAI 키 설정: PowerShell `setx OPENAI_API_KEY "sk-..."` 또는 `week3-practice/.env`에 `OPENAI_API_KEY=...` 추가  
2) (선택) Chroma 실행: `docker run -d --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:latest`  
3) 앱 실행:
```bash
cd week3-practice
./gradlew bootRun
```

## API 엔드포인트 🌐
- `POST /api/devdocs/ingest` : DevDocs 청킹·임베딩 후 벡터스토어 적재, 샘플 5개 반환
- `POST /api/agent/ask` : `{ "question": "..." }` → RAG 에이전트 응답(필요 시 툴 호출 포함)
- `POST /api/openai/chat` : `{ "message": "..." }` → RAG 없이 LLM 직접 호출
- UI: `http://localhost:8080/devdocs.html`

## 코드 맵 🗺️
- 설정: `src/main/java/com/example/llmping/config/` (`OpenAiConfig`, `ChromaConfig`, `AgentConfig`)
- RAG 처리: `service/DevDocsIngestionService.java`, `service/DevDocsRagService.java`
- 툴: `tool/DevDocsTools.java`
- REST: `controller/DevDocsController.java`, `AgentController.java`, `OpenAiChatController.java`
- 에이전트 프롬프트: `agent/DevDocsAssistant.java`
