# Week 3 실습: DevDocs RAG Assistant
Stripe DevDocs를 임베딩해 Chroma(또는 인메모리)에서 검색하고, OpenAI 모델로 답변을 만드는 Spring Boot 3 + LangChain4j 예제입니다. 단일 HTML UI(`src/main/resources/static/devdocs.html`)로 RAG/Raw LLM/Chroma 조회를 볼 수 있습니다.

## 무엇을 하나요?
- DevDocs(기본: `data/devdocs/stripe/**/*.md`)를 chunk→임베딩→저장 후 검색합니다.
- 질문을 던지면 상위 chunk를 찾아 답변을 만들거나, RAG 검색 결과만 확인할 수 있습니다.
- Chroma 컬렉션 목록/프리뷰, Raw LLM 호출, 간단한 에이전트(DevDocsAssistant)도 제공합니다.

## 필요한 것
- Java 21, Gradle 래퍼
- OpenAI API 키 (`OPENAI_API_KEY` 필수)
  - Chat: `gpt-4o-mini`, Embedding: `text-embedding-3-small`
- (선택) Chroma 서버 `http://localhost:8000`
  - 없거나 연결 실패 시 자동으로 인메모리 스토어로 폴백합니다.

## 빠르게 실행
```bash
cd week3-practice
# (.env에 키 저장을 선호하면)
echo OPENAI_API_KEY=sk-... > .env
# 또는 환경변수로 설정 후 실행
# setx OPENAI_API_KEY "sk-..."   # PowerShell 예시

./gradlew bootRun
```
- UI: 브라우저에서 `http://localhost:8080/devdocs.html`
- Chroma를 쓰려면 (선택):
  - Python 가상환경에서: `python -m chromadb --path ./chroma_db --port 8000`
  - Docker를 쓴다면: `docker run -d --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:latest`
  - 둘 다 안 띄우면 자동으로 인메모리 스토어를 사용합니다(`chroma.enabled=false`로 명시도 가능).

## 폴더 구조
```text
week3-practice/
├── data/devdocs/stripe/...     # 기본 인덱싱 대상 MD 문서
├── src/main/java/com/example/devdocs
│   ├── controller               # DevDocs/Agent/OpenAI REST
│   ├── service                  # Ingestion/RAG/Chroma 조회
│   ├── config                   # OpenAI, Chroma 설정
│   ├── tool                     # DevDocs Tools (LangChain4j)
│   └── agent                    # DevDocsAssistant 인터페이스
├── src/main/resources/static/devdocs.html   # 데모 UI
└── README.md
```

## 주요 기능
- **문서 적재**: `POST /api/devdocs/ingest` → `data/devdocs/stripe/**/*.md`를 chunk(기본 300, overlap 80)로 나눠 컬렉션 `devdocs-stripe-300`에 저장.
- **RAG 검색**: `POST /api/devdocs/query` → 질의어 임베딩 후 top-k(기본 3) chunk 반환. `mode=size600`이면 `devdocs-stripe-600` 사용(해당 크기로 먼저 적재 필요).
- **Chroma 인사이트**: 컬렉션 목록/프리뷰로 스토어 상태 확인.
- **Agent/Raw LLM**: `DevDocsAssistant`로 간단 답변, `/api/openai/chat`으로 RAG 없이 LLM만 호출.

## 주요 엔드포인트 (JSON)
- `POST /api/devdocs/ingest`
  - 바디 없음. DevDocs를 읽어 chunk→임베딩→Chroma(또는 인메모리)에 저장. 총 chunk 수와 샘플 5개 반환.
- `POST /api/devdocs/query`
  - `{ "question": "...", "provider": "stripe", "mode": "size300|size600" }`
  - RAG top-k chunk 목록(`text`, `metadata`) 반환. 답변 생성은 하지 않음.
- `GET /api/devdocs/collections`
  - Chroma 컬렉션 이름 목록.
- `GET /api/devdocs/collections/{name}/preview?limit=5`
  - 해당 컬렉션에서 일부 문서/메타데이터 샘플.
- `POST /api/agent/ask`
  - `{ "question": "..." }` → DevDocsAssistant가 간단히 답변(툴 사용 힌트만 있는 상태).
- `POST /api/openai/chat`
  - `{ "message": "..." }` → RAG 없이 OpenAI Chat 결과 반환.

## RAG/스토어 동작
- 컬렉션 선택: `mode=size600`이면 `devdocs-stripe-600`, 그 외 `devdocs-stripe-300` 사용.
- Chroma 비활성(`chroma.enabled=false`)이거나 연결 실패 시 인메모리 스토어로 자동 폴백(로그 경고).
- 디버그: `logging.level.com.example.devdocs.service=DEBUG`로 검색된 chunk/점수/finalPrompt가 `rag_debug=...` JSON으로 로그에 남습니다.

## 문제 해결
- `OPENAI_API_KEY` 누락: 환경변수나 `.env`에 키를 설정하세요.
- Chroma 연결 실패: Docker 컨테이너 상태 확인 또는 `chroma.enabled=false`로 인메모리 사용.
- 데이터 없음: `data/devdocs/stripe`에 MD 파일이 없으면 ingest 결과가 비어 있습니다.

## 추가 메모
- 다른 chunk 크기로 적재하려면 `DevDocsIngestionService.ingestDocs(chunkSize, overlap, collection)`을 호출하는 엔드포인트를 추가하면 됩니다.
- Agent는 현재 RAG 주입 없이 기본 프롬프트만 있습니다. 툴 호출이나 RAG 주입을 넣으려면 `DevDocsAssistant`와 서비스 레이어를 확장하세요.
