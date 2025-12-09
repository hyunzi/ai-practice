# DevDocs RAG Assistant (Week 3)

Stripe DevDocs 마크다운을 쪼개 벡터화하고 RAG 검색을 붙인 Spring Boot + LangChain4j 샘플입니다. 기본 OpenAI 모델은 `gpt-4o-mini` / `text-embedding-3-small`이며, Chroma가 없으면 인메모리 스토어로 자동 폴백합니다. 간단한 HTML 테스트 페이지(`src/main/resources/static/devdocs.html`)로 인젝션·RAG·에이전트·원본 LLM 호출·Chroma 조회를 한 번에 확인할 수 있습니다.

## 주요 기능
- DevDocs 인젝션: `data/devdocs/stripe/**/*.md`를 `chunkSize=300, overlap=80`(기본)으로 분할해 `devdocs-stripe-300` 컬렉션에 저장. `size600` 모드도 선택 가능(`devdocs-stripe-600`).
- RAG 검색: 쿼리를 임베딩 후 상위 K(기본 3) 청크를 반환하고 메타데이터(`provider/section/fileName/chunkIndex`)까지 로그로 남김.
- 에이전트/툴: LangChain4j AiService(`DevDocsAssistant`)가 `searchDevDocs(query, provider, mode)`와 `formatAnswerAsEmail(summary, recipientRole, tone)` 두 가지 툴을 호출해 답변을 구성.
- UI 데모: `/devdocs.html`에서 인젝션 → RAG → 에이전트 → Raw LLM → Chroma 조회까지 실험.
- Chroma 인트로스펙션: 컬렉션 목록 조회 및 문서 샘플 프리뷰 엔드포인트 제공(Chroma HTTP API 사용).

## 아키텍처 한눈에 보기
- 컨피그: `config/OpenAiConfig`, `ChromaConfig`(+`ChromaStoreFactory` 폴백), `AgentConfig`에서 모델/스토어/에이전트 빈을 구성.
- 인젝션: `service/DevDocsIngestionService`가 파일을 청크 → 임베딩 → 컬렉션에 저장.
- 검색: `service/DevDocsRagService`가 쿼리 임베딩 후 top-k 검색 및 디버그 로그(`rag_debug=...`) 출력.
- 툴: `tool/DevDocsTools`에서 RAG 호출과 이메일 포매터 제공.
- REST: `controller/DevDocsController`(ingest/query/Chroma 조회), `AgentController`(에이전트 질문), `OpenAiChatController`(Raw LLM 호출).

## 실행 방법
1) OpenAI 키 설정: PowerShell `setx OPENAI_API_KEY "sk-..."` 또는 `week3-practice/.env`에 `OPENAI_API_KEY=...` 추가.  
2) (선택) Chroma 실행: `docker run -d --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:latest` (없으면 자동 인메모리 사용).  
3) 서버 실행:
```bash
cd week3-practice
./gradlew bootRun
```
4) UI 접근: 브라우저에서 `http://localhost:8080/devdocs.html`.

## 주요 API
- `POST /api/devdocs/ingest` : DevDocs를 청크·임베딩 후 벡터 스토어에 저장(샘플 5개 포함 응답).
- `POST /api/devdocs/query` : `{ question, provider="stripe", mode="size300|size600" }`로 RAG top-k(기본 3) 청크 조회.
- `POST /api/agent/ask` : `{ question, mode }`로 에이전트에게 질의(툴 호출 가능).
- `POST /api/openai/chat` : `{ message }`로 RAG 없이 모델 직접 호출.
- `GET /api/devdocs/collections` : Chroma 컬렉션 이름 목록.
- `GET /api/devdocs/collections/{name}/preview?limit=5` : 지정 컬렉션 문서/메타데이터 샘플 프리뷰.

## 데이터 · 컬렉션
- 원본 위치: `data/devdocs/stripe/**/*.md`
- 기본 컬렉션: `devdocs-stripe-300`(chunk 300), 추가: `devdocs-stripe-600`(chunk 600)
- 메타데이터: `provider`, `section`(상위 폴더), `fileName`, `chunkIndex`

## 로깅
`logging.level.com.example.llmping.service=DEBUG`일 때 `DevDocsRagService`가 최종 프롬프트/쿼리/매치 정보를 `rag_debug=...` 형태로 남깁니다.
