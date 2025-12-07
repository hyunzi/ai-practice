# DevDocs RAG Sample (Spring Boot + LangChain4j)

간단한 Stripe DevDocs RAG 파이프라인 예제입니다. LangChain4j로 OpenAI 모델/임베딩과 Chroma 벡터 스토어를 연동하고, 인제스트/검색/에이전트 호출을 REST와 정적 페이지로 확인할 수 있습니다.

## Stack
- Java 21, Spring Boot 3.5.5, Gradle 9
- LangChain4j 0.33.0: Chat, Embedding, Tools/AiServices
- OpenAI: `gpt-4o-mini`(chat), `text-embedding-3-small`(embedding)
- Vector store: Chroma (`langchain4j-chroma`), fallback to in-memory
- Frontend: 단일 정적 페이지 `static/devdocs.html`

## 구성 개요
- `OpenAiConfig`: Chat/Embedding 모델 빈 제공 (환경변수 `OPENAI_API_KEY` 필요)
- `ChromaConfig`: 기본 `http://localhost:8000` Chroma 연결, 실패 시 인메모리 스토어로 폴백. `chroma.enabled=false`로 강제 인메모리 사용 가능.
- `DevDocsIngestionService`: `data/devdocs/stripe/**/*.md` 읽기 → 300/80 토큰 청킹 → 임베딩 생성 → EmbeddingStore(Chroma)에 저장. 메타데이터(provider/section/fileName/chunkIndex) 포함.
- `DevDocsRagService`: 쿼리 임베딩 후 상위 K(기본 3) 청크 검색.
- Tools (`DevDocsTools`):
  - `searchDevDocs(query, provider)`: RAG 검색 후 상위 3개 청크 내용을 합쳐 반환.
  - `formatAnswerAsEmail(answerSummary, recipientRole, tone)`: 이메일 초안 형식 문자열 생성.
- 에이전트 (`DevDocsAssistant` + `AgentConfig`): LangChain4j AiService에 Chat 모델 + Tools 주입.
- REST
  - `POST /api/devdocs/ingest` : 인제스트 수행, 총 청크 수와 샘플 5개 반환.
  - `POST /api/agent/ask` : `{ "question": "..." }` → 에이전트 응답.
  - `POST /api/openai/chat` : `{ "message": "..." }` → RAG 없이 순수 OpenAI Chat 호출(비교용).
- UI: `http://localhost:8080/devdocs.html` 에서 인제스트/질문을 브라우저로 테스트.

## 사전 준비
1) OpenAI 키 설정  
   - PowerShell: `setx OPENAI_API_KEY "sk-..."` (새 셸 필요)  
   - 또는 `week3-practice/.env`에 `OPENAI_API_KEY=...` 추가 (이미 gitignore 대상)
2) Chroma 준비 (권장: Docker)  
   - `docker run -d --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:latest`
   - 동작 확인: `curl http://localhost:8000/api/v1/collections`
   - Chroma 없이 테스트하려면 `-Dchroma.enabled=false` 또는 `application.yml/.env`에 `chroma.enabled=false`.

## 실행
```bash
cd week3-practice
./gradlew bootRun
```
- 인제스트: `POST http://localhost:8080/api/devdocs/ingest`
- 질의: `POST http://localhost:8080/api/agent/ask` (JSON body: `{"question":"How to update a Stripe payment method?"}`)
- 브라우저: `http://localhost:8080/devdocs.html`

## Chroma 데이터 확인 (옵션)
```bash
# 컬렉션 목록
curl http://localhost:8000/api/v1/collections
# 특정 컬렉션 조회
curl "http://localhost:8000/api/v1/collections/devdocs-stripe"
# 샘플 포함 조회
curl -X POST http://localhost:8000/api/v1/collections/devdocs-stripe/get \
  -H "Content-Type: application/json" \
  -d '{"limit":5,"include":["documents","metadatas"]}'
```

## 테스트
```bash
./gradlew test
```
테스트에서는 `chroma.enabled=false`로 인메모리 스토어를 사용합니다.

## 주요 파일
- `src/main/java/com/example/llmping/config/`  
  - `OpenAiConfig.java` (Chat/Embedding 모델)  
  - `ChromaConfig.java` (Chroma 또는 인메모리 EmbeddingStore)  
  - `AgentConfig.java` (AiService + Tools)
- `src/main/java/com/example/llmping/service/`  
  - `DevDocsIngestionService.java`, `DevDocsRagService.java`
- `src/main/java/com/example/llmping/tool/DevDocsTools.java`
- `src/main/java/com/example/llmping/controller/`  
  - `DevDocsController.java` (/api/devdocs/ingest)  
  - `AgentController.java` (/api/agent/ask)
- `src/main/resources/static/devdocs.html` (브라우저 테스트 페이지)
