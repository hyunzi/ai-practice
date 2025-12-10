# Mail Assistant 기능

Mail Assistant가 무엇을 어떻게 호출하는지 한눈에 보이도록 정리했습니다.

## 핵심 흐름
- 입력 → 컨트롤러 → 서비스에서 프롬프트 문자열 조립 → LLM 한 번 호출 → 응답 반환.
- UI(`index.html`)는 fetch로 REST API를 부르고, 결과 JSON을 그대로 화면에 보여줍니다.

## LLM 설정
- Ollama Chat 모델 두 개(`emailAgentModel`, `devDocAgentModel`)를 Bean으로 분리 주입.
  - 공통: `baseUrl=http://localhost:11434`, `modelName=llama3.2:3b`
  - 차이: temperature(이메일 0.3, 개발 문서 0.2)
- `LlmConfig`에 정의되어 있고, 서비스에서 `ChatLanguageModel.generate(prompt)`로 직접 호출합니다.

## LangChain4j 사용 지점
- 체인/메모리/툴은 쓰지 않고, 프롬프트 엔지니어링 + 단일 `generate` 호출만 사용.
- `EmailAgentServiceImpl`
  - `generateDraft`: tone/mailType/컨텍스트를 포함해 영문 비즈니스 메일 작성. mailType=NEW/REPLY/APOLOGY/REMINDER일 때 구조 힌트 추가. AUTO는 아직 분류 안 함.
  - `checkDraft`: 하드코딩된 체크리스트(한글)와 메일을 함께 보내 개선 제안 bullet을 요청. 결과 전체를 `suggestions` 1개 항목으로 담고, `missingItems`는 플레이스홀더.
- `DevDocAgentServiceImpl`
  - `answerQuestion`: Summary / Detailed Answer / Referenced Snippets 섹션을 요구하는 Markdown 포맷을 프롬프트로 강제. 비어 있으면 기본 문구로 채움.
  - `generateStudyNote`: 제목/요약/핵심 개념/실전 활용 예/TODO 섹션을 가진 Markdown 노트 생성.
- `MailAssistantController.chat`: 가장 단순한 예시. 한글 입력을 받아 정중한 한글 메일을 만들어 반환.

## RAG 상태
- 임베딩 모델: `nomic-embed-text`(Ollama) Bean (`RagConfig`).
- 임베딩 스토어: In-memory store를 `EmailRagService`, `DevDocRagService`가 보유.
- 두 RAG 서비스 모두 `indexTexts(List<String>)`로 텍스트를 넣고, `retrieveRelevantTexts(query, topK)`로 검색할 수 있음.
- 현재 서비스 로직에서는 RAG 결과를 사용하지 않음(주석으로 확장 포인트만 존재). 실제 데이터가 없고, 프롬프트 주입 코드도 주석 처리되어 있음.

## HTTP/UI 흐름
- UI: `index.html`이 fetch로 아래 REST를 호출, 응답 JSON을 그대로 표시하고 초안 결과를 체크리스트 입력란에 복사.
- 엔드포인트:
  - `/api/assistant/chat` : 한글 입력 → 정중한 한글 메일
  - `/api/agent/email/draft` : 한글 초안 → 영문 메일(+tone/mailType 반영)
  - `/api/agent/email/checklist` : 영문 메일 → 체크리스트 기반 제안
  - `/api/agent/devdoc/qa` : 개발 Q&A → Summary/Detailed/Referenced Snippets
  - `/api/agent/devdoc/note` : 주제별 Markdown 학습 노트
- 테스트: 컨트롤러 테스트에서 `@MockBean`으로 LLM을 대체하여 Ollama 없이 실행.

## 앞으로 확장 아이디어
- mailType=AUTO 자동 분류, 체크리스트 결과를 항목별 배열로 파싱.
- `indexTexts`를 이용해 내부 예시/문서를 임베딩 후 RAG 결과를 프롬프트에 주입.
- LangChain4j 도구 호출/워크플로우를 붙여 멀티스텝(분류 → 템플릿 → 검수) 구성.
