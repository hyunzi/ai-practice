# WEEK1 실습

## 개요
- **Fruit Calendar**: 1주차 첫 실습은 과일 캘린더이며, `week1-practice/fruit-calendar` 아래에서 관련 설정과 자료를 그대로 확인할 수 있습니다.
- **Mini Readers**: Spring Boot 3.5 기반 REST API로, 명언을 추가·조회하고 공유 카드 이미지를 생성하는 과정을 담았습니다. 코드와 DB는 `week1-practice/mini-readers` 아래에 있습니다.
- **Playwright 캡처**: Mini Readers의 공유 카드를 빠르게 캡처하기 위한 Playwright 스크립트가 함께 들어 있습니다.

## 폴더 구조
```text
ai-practice/
├── week1-practice/                 # Week 1 실습 콘텐츠 (Fruit Calendar + Mini Readers)
│   ├── fruit-calendar/             # Week 1 과일 캘린더 실습
│   └── mini-readers/                # Mini Readers REST API (archived but runnable)
│       ├── src/main/java
│       ├── src/main/resources
│       ├── data/mini-readers.db
│       ├── playwright/capture.spec.ts
│       ├── pom.xml
├── week2-practice/                 # 2주차 실습을 위한 새 공간
│   └── README.md
├── mcp/mini-readers-openapi.yaml   # MCP(OpenAPI) 실습 스펙
├── package.json / package-lock.json  # Playwright 실행용 Node.js 설정
├── README.md
```

## 현재 정리
- Week 1 실습은 `week1-practice/` 아래에서 Fruit Calendar와 Mini Readers로 구분되어 있으며, 각 디렉터리에서 기존대로 빌드·실행이 가능합니다.
- 2주차 실습은 `week2-practice/` 아래에서 새로 작성하면서, 필요한 안내와 기록은 해당 README에 차근차근 남겨 주세요.

## Mini Readers 실행 가이드
1. **필수 조건**
   - JDK 21, Maven 3.9+, Playwright와 함께 쓰려면 Node.js 18+가 필요합니다.
   - `week1-practice/mini-readers/data/mini-readers.db`는 개발용 샘플 DB입니다. 필요하면 삭제 후 `mvn spring-boot:run`으로 새 DB를 생성할 수 있습니다.
2. **빌드·실행**
   ```bash
   cd week1-practice/mini-readers
   mvn clean package
   java -jar target/mini-readers-0.0.1-SNAPSHOT.jar
   ```
   개발 중에는 `mvn spring-boot:run`을 사용하면 빠르게 재시작할 수 있습니다.
3. **주요 API**
   - `GET /api/quotes`: 최근 구절 조회
   - `POST /api/quotes`: 구절 추가 (`QuoteRequest` DTO 사용)
   - `GET /api/quotes/{id}/share`: 공유 카드 메시지 반환

## MCP(OpenAPI) 실습 가이드
1. VS Code MCP 확장 또는 Claude Desktop 같은 MCP 클라이언트에서 `mcp/mini-readers-openapi.yaml`을 등록하세요.
2. 로컬 서버 URL은 `http://localhost:8080`입니다.
3. 실습 흐름
   1. `POST /api/quotes`로 구절을 입력합니다.
      ```bash
      curl -X POST http://localhost:8080/api/quotes \
        -H "Content-Type: application/json" \
        -d '{
              "passage": "작은 변화가 큰 길을 만듭니다.",
              "note": "기분을 적어두면 다음에 되돌아보기 좋아요.",
              "moods": ["차분", "기대"]
            }'
      ```
   2. OpenAPI 명세를 보고 `GET /api/quotes`나 `GET /api/quotes/{id}/share`에 요청을 보내면 됩니다.
4. 서버 주소가 바뀌면 `mini-readers-openapi.yaml`의 `servers` 항목을 업데이트하세요.

## Playwright 캡처 워크플로
1. 백엔드를 먼저 실행합니다(`mvn spring-boot:run` 또는 위 빌드·실행 명령).
2. Playwright를 처음 설치할 때:
   ```bash
   cd week1-practice/mini-readers
   npm install
   npx playwright install
   ```
3. 캡처 실행:
   ```bash
   MINI_READERS_URL=http://localhost:8080 \
   npx playwright test playwright/capture.spec.ts --project=chromium
   ```
   생성된 이미지는 `week1-practice/mini-readers/playwright/screenshots/mini-readers.png`에 저장됩니다.
4. `--headed` 옵션이나 `trace`를 남기면 `trace.zip`이 생성되며, `npx playwright show-trace trace.zip`으로 확인하세요.

## 문제 해결
- Windows에서 `cmd.exe CreatePipe error=5`가 발생하면 `mvn clean package -DskipTests`로 빌드하거나 PowerShell을 이용해 보세요.
- SQLite 파일이 잠겨 있다면 애플리케이션을 종료하고 `week1-practice/mini-readers/data/mini-readers.db`를 삭제(또는 백업)한 뒤 다시 실행하면 새 DB가 생성됩니다.
