# Dev Docs RAG 아키텍처

## 1. 전체 흐름
- 사용자 → `/api/devdocs/ingest`(선택) → 마크다운 청크/임베딩 → Chroma(또는 인메모리) 저장
- 사용자 → `/api/devdocs/query` → RAG 검색 → 상위 청크/메타데이터 반환
- 사용자 → `/api/agent/ask` → LangChain4j 에이전트 → 필요 시 `searchDevDocs` 툴 호출 → 답변(또는 이메일 포맷)
- `/api/openai/chat` 으로 RAG 없이 모델 직접 호출 가능

## 2. 구성 요소
- **Model**: Chat `gpt-4o-mini`, Embedding `text-embedding-3-small` (`config/OpenAiConfig`)
- **Memory**: 상태 저장 없음(요청 단위 처리)
- **Tools (`tool/DevDocsTools`)**
  - `searchDevDocs(query, provider, mode)`: RAG로 top-3 청크 검색 후 텍스트 연결
  - `formatAnswerAsEmail(answerSummary, recipientRole, tone)`: 요약을 이메일 초안 형태로 포매팅
- **Vector Store**: 기본 Chroma HTTP(`chroma.base-url`), 실패/비활성 시 인메모리(`ChromaStoreFactory`)
  - 컬렉션: `devdocs-stripe-300`, `devdocs-stripe-600`
  - 메타데이터: `provider`, `section`, `fileName`, `chunkIndex`

## 3. 주요 워크플로우
- **인젝션 (`service/DevDocsIngestionService`)**
  - 입력: `data/devdocs/stripe/**/*.md`
  - 분할: `DocumentSplitters.recursive(chunkSize, 80, OpenAiTokenizer("gpt-4o-mini"))`
  - 저장: 임베딩 생성 → 지정 컬렉션(`devdocs-stripe-300` 기본) → 메타데이터 부착
- **검색 (`service/DevDocsRagService`)**
  - 쿼리 임베딩 → `EmbeddingStore.findRelevant(topK)`
  - 모드별 컬렉션 선택: `size300` → `devdocs-stripe-300`, `size600` → `devdocs-stripe-600`(기타는 기본 300)
  - 디버그: `rag_debug=...` 로그에 최종 프롬프트/쿼리/매치 프리뷰 출력
- **에이전트 (`config/AgentConfig`, `agent/DevDocsAssistant`)**
  - `@SystemMessage`로 Stripe DevDocs 검색 도우미 역할 지정
  - 필요 시 툴 호출 → LLM이 최종 응답 생성

## 4. 인터페이스 & 도구
- **REST**: `DevDocsController`(ingest/query/collections), `AgentController`(에이전트), `OpenAiChatController`(raw chat)
- **UI**: `src/main/resources/static/devdocs.html`에서 인젝션/RAG/에이전트/Chroma 프리뷰 테스트
- **설정**: `application.yml`에서 OpenAI 키, Chroma base-url/collection, 로그 레벨 조정
