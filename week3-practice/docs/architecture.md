# DevDocs RAG 아키텍처 & 기능 개요

## 큰 흐름
```
요청 → 컨트롤러 → 서비스에서 프롬프트/쿼리 구성 → LLM/RAG 호출 → 응답
```
- `/api/devdocs/ingest` : DevDocs 마크다운 chunk→임베딩→저장(Chroma 또는 인메모리).
- `/api/devdocs/query` : 질의 임베딩 → top-k chunk 반환(답변 생성 없음).
- `/api/agent/ask` : DevDocsAssistant(AiService) → 필요 시 RAG 툴 호출 → 답변(베이스라인).
- `/api/openai/chat` : RAG 없이 OpenAI Chat 직접 호출.

## 모델 / 메모리 / 스토어
- **Chat 모델**: `gpt-4o-mini` (`config/OpenAiConfig`), `ChatLanguageModel`로 주입.
- **Embedding 모델**: `text-embedding-3-small` (`config/OpenAiConfig`), RAG/ingest 모두 사용.
- **스토어**: 기본 Chroma HTTP(`chroma.base-url`). 실패/비활성 시 `InMemoryEmbeddingStore`로 폴백(`ChromaStoreFactory`). 컬렉션 `devdocs-stripe-300`, `devdocs-stripe-600`; 메타데이터 `provider/section/fileName/chunkIndex`.

## Tools (Function Calling)
- 위치: `config/AgentConfig` + `tool/DevDocsTools`.
- `searchDevDocs(query, provider, mode)`: `DevDocsRagService.searchTopChunks(..., topK=3, mode=size300|size600, provider=default "stripe")`.
- `formatAnswerAsEmail(summary, role, tone)`: 요약을 이메일 형태로 다듬는 보조 함수(옵션).

## 에이전트
- `DevDocsAssistant` @SystemMessage: “Stripe devdocs를 검색해 친절히 답변”.
- @UserMessage: 사용자가 던지는 질문. 필요 시 위 Tools를 호출해 RAG 결과를 섞어 답변하도록 설계(현재는 베이스라인).
- **바인딩(`config/AgentConfig`)**: `AiServices.builder(DevDocsAssistant.class)`에 `chatLanguageModel`과 `DevDocsTools`를 연결해 LangChain4j 함수 호출을 활성화합니다. Tools는 RAG 검색·이메일 포맷터 두 함수만 등록된 최소 구성입니다.

## 적재(ingest) 파이프라인
- 입력: `data/devdocs/stripe/**/*.md`
- 분할: `DocumentSplitters.recursive(chunkSize, 80, OpenAiTokenizer("gpt-4o-mini"))`
- 저장: 컬렉션 지정(기본 `devdocs-stripe-300`) 후 임베딩/세그먼트 저장.

## RAG 검색 파이프라인
- `EmbeddingStore.findRelevant(topK)`로 top-k chunk 선택.
- `mode`로 컬렉션 선택: `size300` → `devdocs-stripe-300`, `size600` → `devdocs-stripe-600`.
- DEBUG: `logging.level.com.example.devdocs.service=DEBUG` 시 `rag_debug=...` JSON 로그로 최종 프롬프트/쿼리/매치 정보 출력.

## REST & UI
- REST: `DevDocsController`(ingest/query/collections), `AgentController`(agent ask), `OpenAiChatController`(raw chat).
- UI: `src/main/resources/static/devdocs.html`에서 적재/RAG/에이전트/Chroma 조회 실행.
- 설정: `application.yml`에서 OpenAI 키, Chroma base-url/collection, 로깅 레벨 관리.

## 엔드포인트 요약
- `POST /api/devdocs/ingest` : 기본 300 컬렉션에 적재, 샘플 5개와 총 chunk 수 반환.
- `POST /api/devdocs/query` : `{ question, provider, mode }` → top-3 chunk 반환.
- `POST /api/agent/ask` : `{ question, mode }` → DevDocsAssistant 답변.
- `POST /api/openai/chat` : `{ message }` → RAG 없이 Chat 호출.
- `GET /api/devdocs/collections`, `GET /api/devdocs/collections/{name}/preview` : Chroma 컬렉션 목록/프리뷰 조회.
