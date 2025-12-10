# Week 2 실습: Mail Assistant
로컬 LLM(Ollama)과 LangChain4j를 이용해 이메일 초안/체크리스트와 개발 문서 Q&A/노트를 생성하는 Spring Boot 3.2.2 앱입니다. UI는 `mail-assistant/src/main/resources/static/index.html` 하나로 동작합니다.

## 무엇을 하나요?
- **Mail Assistant**: 한글 입력을 받아 정중한 메일을 만들어주는 간단한 챗 엔드포인트.
- **Email Agent**: 한글 초안을 영문 비즈니스 메일로 변환하고 체크리스트로 검수.
- **DevDoc Agent**: 개발 질문에 대한 Q&A, 주제별 학습 노트를 Markdown으로 생성.

## 필요한 것
- Java 21, Gradle 래퍼
- Ollama 서버 (`http://localhost:11434`)
  - `ollama pull llama3.2:3b`
  - `ollama pull nomic-embed-text`

## 빠르게 실행
```bash
cd week2-practice/mail-assistant
.\gradlew.bat bootRun    # Windows (macOS/Linux는 ./gradlew bootRun)
```
- 기본 포트: 8080
- 브라우저에서 `http://localhost:8080/`로 접속 (단일 페이지 UI).

## 폴더 구조
```text
week2-practice/
├── mail-assistant/
│   ├── src/main/java/...       # 컨트롤러, 서비스, LLM/RAG 설정
│   ├── src/main/resources/...  # 정적 UI(index.html), 설정
│   ├── test/                   # 컨트롤러 테스트(MockMvc + MockBean)
│   ├── function.md             # LLM/RAG 적용 상세 메모
│   └── README.md
└── README.md
```

## 주요 엔드포인트 (모두 POST, JSON)
- `/api/assistant/chat`  
  - 입력: `{ "message": "<한글>" }`(필수) → 출력: `{ "reply": "<정중한 한글 메일>" }`
- `/api/agent/email/draft`  
  - 입력: `koreanDraft` 필수, `tone` 기본 `soft`, `mailType` 기본 `AUTO`, `additionalContext` 선택  
  - 출력: `{ finalEnglishMail, detectedMailType, appliedTone }` (AUTO라도 분류는 아직 하지 않고 그대로 반환)
- `/api/agent/email/checklist`  
  - 입력: `englishMail` 필수  
  - 출력: `missingItems`(현재 고정 1개 플레이스홀더), `suggestions`(LLM 응답 전체가 1개 항목으로 담김)  
  - 체크리스트 문구는 코드에 박힌 한글 텍스트를 그대로 사용합니다.
- `/api/agent/devdoc/qa`  
  - 입력: `question` 필수, `persona`/`topicHint` 선택  
  - 출력: `{ summary, detailedAnswer, referencedSnippets }`  
  - LLM에게 Summary/Detailed Answer/Referenced Snippets 섹션을 요구하고, 비어 있으면 기본 문구로 채웁니다.
- `/api/agent/devdoc/note`  
  - 입력: `topic` 필수, `persona` 선택  
  - 출력: `{ markdownNote }` (고정된 섹션 헤더 포함)

## RAG 상태
- In-memory 임베딩 스토어 + `nomic-embed-text` 임베딩 모델 Bean이 준비돼 있습니다.
- 실제 서비스 로직에서는 아직 RAG 결과를 쓰지 않습니다(주석으로만 확장 포인트 존재). 데이터를 쓰려면 `EmailRagService.indexTexts` / `DevDocRagService.indexTexts`를 직접 호출해야 합니다.

## 테스트
```bash
cd week2-practice/mail-assistant
.\gradlew.bat test
```
컨트롤러 테스트는 `@MockBean`으로 LLM을 대체하므로 Ollama 없이도 실행됩니다.

## 추가 문서
- `mail-assistant/function.md`: LLM/RAG 적용 방식과 확장 아이디어를 자세히 정리한 메모입니다.
