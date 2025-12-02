# Mail Assistant (Week 2 Practice)

로컬 LLM(Ollama)과 LangChain4j를 이용해,

- 💌 영어 메일 도우미 (Email Agent A)
- 📖 개발 문서/가이드 리더 (DevDoc Agent B)

를 제공하는 Spring Boot 3 애플리케이션입니다.  
간단한 정적 웹 UI(`index.html`)와 REST API로 에이전트를 호출할 수 있습니다.

---

## 기술 스택

- Java 21
- Spring Boot 3.2.2
- Gradle (Kotlin DSL, `build.gradle.kts`)
- LangChain4j 0.36.0
  - `langchain4j`
  - `langchain4j-ollama`
  - (옵션) `langchain4j-open-ai`
- Lombok
- JUnit 5 + Spring Boot Test + MockMvc
- LLM 서버: [Ollama](https://ollama.com/) (로컬 실행)

---

## 프로젝트 구조

루트: `week2-practice/mail-assistant`

```text
src
 ├─ main
 │   ├─ java/com/example/agents
 │   │   ├─ LocalAiAgentsApplication.java      # Spring Boot 엔트리 포인트
 │   │   ├─ api                               # (기존) 단일 MailAssistant API
 │   │   ├─ config                            # Ollama 관련 공통 설정
 │   │   ├─ llm
 │   │   │   └─ LlmConfig.java                # Email / DevDoc용 ChatLanguageModel Bean
 │   │   ├─ email
 │   │   │   ├─ api                           # EmailAgentController
 │   │   │   ├─ model                         # Email Agent DTO들
 │   │   │   └─ service                       # EmailAgentService (+ 구현체)
 │   │   ├─ devdoc
 │   │   │   ├─ api                           # DevDocAgentController
 │   │   │   ├─ model                         # DevDoc Agent DTO들
 │   │   │   └─ service                       # DevDocAgentService (+ 구현체)
 │   │   └─ rag
 │   │       ├─ common                        # RagService 인터페이스 + EmbeddingModel 설정
 │   │       ├─ email                         # EmailRagServiceImpl (스켈레톤)
 │   │       └─ devdoc                        # DevDocRagServiceImpl (스켈레톤)
 │   └─ resources
 │       └─ static
 │           └─ index.html                    # 간단한 웹 UI (탭: Email / DevDoc)
 └─ test/java/com/example/agents
     ├─ email/api/EmailAgentControllerTest.java
     ├─ devdoc/api/DevDocAgentControllerTest.java
     └─ rag/...                               # RAG 관련 테스트 예시
```

---

## 빌드 및 실행

### 1. 선행 준비 (Ollama)

1. Ollama 설치 후 실행:
   - https://ollama.com/ 에서 OS에 맞는 설치 파일 다운로드
2. 모델 다운로드:
   ```bash
   ollama pull llama3.2:3b
   ollama pull nomic-embed-text
   ```
3. Ollama 서버가 기본 주소(`http://localhost:11434`)에서 실행 중인지 확인합니다.

### 2. 애플리케이션 실행

프로젝트 루트에서:

```bash
cd week2-practice/mail-assistant

# Windows PowerShell
.\gradlew.bat bootRun

# macOS / Linux (참고)
./gradlew bootRun
```

기본 포트는 `8080`입니다.

### 3. 웹 UI 접속

브라우저에서:

```text
http://localhost:8080/
```

정적 리소스(`src/main/resources/static/index.html`) 기반의 간단한 UI가 열립니다.

- Email Agent 탭
  - 한글 초안, tone, mailType, 추가 배경 입력
  - 버튼:
    - `영어 메일 초안 생성` → `/api/agent/email/draft`
    - `체크리스트 확인` → `/api/agent/email/checklist`
- DevDoc Agent 탭
  - question / persona / topicHint 입력
  - 버튼:
    - `Q&A 실행` → `/api/agent/devdoc/qa`
    - `학습 노트 생성` → `/api/agent/devdoc/note`

---

## LLM 설정 (LlmConfig)

파일: `src/main/java/com/example/agents/llm/LlmConfig.java`

- `emailAgentModel` Bean
  - 타입: `dev.langchain4j.model.chat.ChatLanguageModel`
  - 구현: `OllamaChatModel.builder()`
    - `baseUrl("http://localhost:11434")`
    - `modelName("llama3.2:3b")`
    - `temperature(0.3)`
- `devDocAgentModel` Bean
  - 동일한 OllamaChatModel 기반
  - DevDoc Agent에서 재사용

Email/DevDoc 서비스는 각각 `@Qualifier("emailAgentModel")`, `@Qualifier("devDocAgentModel")` 로 해당 Bean을 주입받습니다.

---

## RAG 스켈레톤 구조

현재는 RAG 동작 자체는 구현되지 않았고, 추후 확장을 위한 뼈대만 존재합니다.

### 공통 인터페이스

파일: `src/main/java/com/example/agents/rag/common/RagService.java`

```java
public interface RagService {
    List<String> retrieveRelevantTexts(String query, int topK);
}
```

### Email RAG

파일: `src/main/java/com/example/agents/rag/email/EmailRagServiceImpl.java`

- `@Service("emailRagService")`로 등록
- 현재 구현:
  ```java
  @Override
  public List<String> retrieveRelevantTexts(String query, int topK) {
      return Collections.singletonList("TODO: implement RAG using local email examples");
  }
  ```
- TODO:
  - `data/email-examples` 폴더의 예시 메일을 인덱싱
  - LangChain4j EmbeddingStore / EmbeddingModel 기반 검색 구현

### DevDoc RAG

파일: `src/main/java/com/example/agents/rag/devdoc/DevDocRagServiceImpl.java`

- `@Service("devDocRagService")`로 등록
- 현재 구현:
  ```java
  @Override
  public List<String> retrieveRelevantTexts(String query, int topK) {
      return Collections.singletonList("TODO: implement RAG using local dev docs");
  }
  ```
- TODO:
  - `data/dev-docs` 폴더 내 개발 문서 인덱싱
  - Embedding 기반 유사도 검색 후 LLM 컨텍스트에 주입

### RAG 주입 지점 (예시)

- `EmailAgentServiceImpl`
  - `Optional<RagService> emailRagService` 주입 (`@Qualifier("emailRagService")`)
  - 주석으로 프롬프트에 예시 이메일 텍스트를 섞는 위치 표시
- `DevDocAgentServiceImpl`
  - `Optional<RagService> devDocRagService` 주입 (`@Qualifier("devDocRagService")`)
  - Q&A / 학습 노트 생성 시, 로컬 문서 스니펫을 혼합할 수 있는 위치를 주석으로 표시

현재 코드는 RAG 서비스를 호출하지 않으므로, RAG 미구현 상태에서도 앱이 정상 동작합니다.

---

## Email Agent A (영어 메일 도우미)

### DTO

패키지: `com.example.agents.email.model`

- `EmailDraftRequest`
  - `String koreanDraft` (필수, `@NotBlank`)
  - `String tone` (`"soft"`, `"formal"`, `"direct"` 등)
  - `String mailType` (`"AUTO"`, `"NEW"`, `"REPLY"`, `"APOLOGY"`, `"REMINDER"` 등)
  - `String additionalContext`
- `EmailDraftResponse`
  - `String finalEnglishMail`
  - `String detectedMailType`
  - `String appliedTone`
- `EmailChecklistRequest`
  - `String englishMail` (필수, `@NotBlank`)
- `EmailChecklistResponse`
  - `List<String> missingItems`
  - `List<String> suggestions`

### 서비스

패키지: `com.example.agents.email.service`

- `EmailAgentService`
  - `EmailDraftResponse generateDraft(EmailDraftRequest request);`
  - `EmailChecklistResponse checkDraft(EmailChecklistRequest request);`
- `EmailAgentServiceImpl`
  - `ChatLanguageModel emailAgentModel` 사용
  - (옵션) `Optional<RagService> emailRagService` 주입

**generateDraft** 요약:

- 역할:
  - “한국인 백엔드 개발자의 영어 비즈니스 메일을 자연스럽게 다듬어주는 도우미”
- 프롬프트 구성 요소:
  - Korean draft
  - tone (soft/formal/direct)
  - mailType (NEW/REPLY/APOLOGY/REMINDER/AUTO)
  - additionalContext
- mailType이 AUTO가 아닐 경우, 유형별 메일 구조 힌트를 추가
- LLM 호출 결과를 `finalEnglishMail`로 사용
- `detectedMailType`:
  - 현재는 AUTO라도 별도 분류 없이 그대로 반환 (추후 분류 로직 확장 가능)
- `appliedTone`:
  - 요청 값 그대로 반환

**checkDraft** 요약:

- 체크리스트(하드코딩):
  - 수신자/참조
  - 메일을 보내는 이유/배경
  - 요청사항/next action
  - 데드라인/일정
  - 마무리 인사/서명
- 영어 메일을 전달하고,
  - 체크리스트 기준으로 누락/보완할 점을 bullet로 정리해 달라고 LLM에 요청
- 응답 처리:
  - `suggestions`: LLM 응답 전체(문자열)를 리스트 한 요소로 넣음
  - `missingItems`: 요약 안내 문구 한 줄만 넣는 간단 버전

### 컨트롤러

패키지: `com.example.agents.email.api`

파일: `EmailAgentController.java`

- `POST /api/agent/email/draft`
  - Request Body: `EmailDraftRequest` (JSON)
  - Response Body: `EmailDraftResponse` (JSON)
- `POST /api/agent/email/checklist`
  - Request Body: `EmailChecklistRequest`
  - Response Body: `EmailChecklistResponse`
- `@Valid`를 통해 `koreanDraft` / `englishMail` 필수 검증 수행

### 예시 호출 (cURL)

```bash
curl -X POST http://localhost:8080/api/agent/email/draft \
  -H "Content-Type: application/json" \
  -d '{
    "koreanDraft": "안녕하세요, 지난주에 논의한 결제 API 변경 건 관련해서 다시 공유드립니다.",
    "tone": "formal",
    "mailType": "NEW",
    "additionalContext": "수신자는 외부 파트너사 PM입니다."
  }'
```

---

## DevDoc Agent B (개발 문서/가이드 리더)

### DTO

패키지: `com.example.agents.devdoc.model`

- `DevDocQuestionRequest`
  - `String question` (필수, `@NotBlank`)
  - `String persona` (예: `"Java 백엔드 10년차 개발자 기준으로 설명해줘"`)
  - `String topicHint` (예: `"webhook"`, `"error handling"`)
- `DevDocAnswerResponse`
  - `String summary`
  - `String detailedAnswer`
  - `List<String> referencedSnippets`
- `DevDocNoteRequest`
  - `String topic` (필수, `@NotBlank`)
  - `String persona`
- `DevDocNoteResponse`
  - `String markdownNote`

### 서비스

패키지: `com.example.agents.devdoc.service`

- `DevDocAgentService`
  - `DevDocAnswerResponse answerQuestion(DevDocQuestionRequest request);`
  - `DevDocNoteResponse generateStudyNote(DevDocNoteRequest request);`
- `DevDocAgentServiceImpl`
  - `ChatLanguageModel devDocAgentModel`
  - (옵션) `Optional<RagService> devDocRagService`

**answerQuestion** 요약:

- 역할:
  - “Java 백엔드 10년차 개발자에게 실무 친화적으로 설명하는 시니어 엔지니어”
- 프롬프트:
  - persona / topicHint / question 반영
  - 응답 포맷을 Markdown 섹션으로 강제:
    - `### Summary`
    - `### Detailed Answer`
    - `### Referenced Snippets`
- 응답 후 처리:
  - 섹션별 텍스트 파싱 (`extractSection`)
  - bullet 리스트 파싱 (`parseBulletLines`) → `referencedSnippets`
  - 파싱 실패 시 간단한 fallback 메시지 사용

**generateStudyNote** 요약:

- `topic` + `persona` 기반으로 Markdown 학습 노트를 생성:
  - `# 제목`
  - `## 요약`
  - `## 핵심 개념`
  - `## 실무 적용 포인트`
  - `## TODO / Follow-up`
- 전체 문자열을 `markdownNote`에 담아 반환

### 컨트롤러

패키지: `com.example.agents.devdoc.api`

파일: `DevDocAgentController.java`

- `POST /api/agent/devdoc/qa`
  - Request Body: `DevDocQuestionRequest`
  - Response Body: `DevDocAnswerResponse`
- `POST /api/agent/devdoc/note`
  - Request Body: `DevDocNoteRequest`
  - Response Body: `DevDocNoteResponse`
- `@Valid`로 `question`, `topic` 필수 검증

### 예시 호출 (cURL)

```bash
curl -X POST http://localhost:8080/api/agent/devdoc/qa \
  -H "Content-Type: application/json" \
  -d '{
    "question": "이 시스템의 webhook 에러 핸들링 개념을 요약해줘",
    "persona": "Java 백엔드 10년차 개발자 기준으로 설명해줘",
    "topicHint": "webhook, error handling"
  }'
```

---

## 테스트

### EmailAgentControllerTest

파일: `src/test/java/com/example/agents/email/api/EmailAgentControllerTest.java`

- `@SpringBootTest`, `@AutoConfigureMockMvc`
- `@MockBean(name = "emailAgentModel") ChatLanguageModel` 으로 실제 Ollama 호출을 막고 빠른 테스트 유지
- `/api/agent/email/draft`에 대해
  - 상태 코드 200 OK
  - 응답 JSON에 `finalEnglishMail` 필드 존재 여부만 간단히 검증

### DevDocAgentControllerTest

파일: `src/test/java/com/example/agents/devdoc/api/DevDocAgentControllerTest.java`

- `@MockBean(name = "devDocAgentModel") ChatLanguageModel`
- `/api/agent/devdoc/qa`에 대해
  - 상태 코드 200 OK
  - `summary`, `detailedAnswer` 필드 존재 여부를 검증

### 테스트 실행

```bash
cd week2-practice/mail-assistant

# Windows
.\gradlew.bat test

# macOS / Linux (참고)
./gradlew test
```

---

## 앞으로의 확장 아이디어

- RAG 실제 구현
  - `data/email-examples`, `data/dev-docs` 폴더 인덱싱
  - LangChain4j EmbeddingStore 활용해 관련 예시/문서를 검색 후, 프롬프트에 컨텍스트로 주입
- Email Agent 고도화
  - `mailType = AUTO`일 때, LLM으로 유형 분류 후 `detectedMailType`에 실제 감지 결과 설정
  - 체크리스트 결과를 구조화된 JSON으로 파싱해 `missingItems`를 더 정교하게 채우기
- DevDoc Agent 고도화
  - DevDoc RAG와 결합해, 팀 내 개발 문서/가이드에 기반한 답변 제공
  - 학습 노트 템플릿을 더 세분화하거나, 수준(초급/중급/고급) 옵션 추가

이 README를 기준으로, 로컬 Ollama + Spring Boot + LangChain4j 기반 에이전트 실험을 계속 확장해 나갈 수 있습니다.

