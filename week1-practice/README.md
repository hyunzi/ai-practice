# Week 1 실습
과일 스탬프 달력(정적 웹)과 Mini Readers(Sprint Boot + SQLite) 두 가지 예제를 포함합니다. 둘 다 바로 실행해볼 수 있도록 단순하게 적었습니다.

## 무엇을 하나요?
- **Fruit Calendar**: 좋아하는 과일 아이콘을 골라 달력 날짜에 찍어두는 간단한 웹앱.
- **Mini Readers**: 마음에 남은 문장을 저장하고 공유 카드 형태로 보여주는 REST API + 정적 웹 UI.

## 필요한 것
- Fruit Calendar: 브라우저만 있으면 됩니다.
- Mini Readers: JDK 21, Maven 3.9+ (SQLite는 내장 JDBC로 자동 생성).

## 빠르게 실행
### Fruit Calendar
```bash
cd week1-practice/fruit-calendar
# 브라우저로 index.html 열기 (또는 임시 서버)
npx http-server .   # 없다면 다른 정적 서버를 써도 됩니다
```

### Mini Readers
```bash
cd week1-practice/mini-readers
mvn spring-boot:run            # 개발 실행 (기본 포트 8080)
# 또는 빌드 후 실행
mvn clean package
java -jar target/mini-readers-0.0.1-SNAPSHOT.jar
```
- DB 파일(`data/mini-readers.db`)이 없으면 실행 시 자동 생성됩니다.

## 폴더 구조
```text
week1-practice/
├── fruit-calendar/           # 정적 달력 앱 (HTML/CSS/JS)
├── mini-readers/             # REST API + 정적 UI
│   ├── src/main/java/...
│   ├── src/main/resources/...
│   ├── data/mini-readers.db  # 없으면 자동 생성
│   └── pom.xml
└── README.md
```

## Fruit Calendar 기능
- 과일 아이콘을 선택하고 날짜를 클릭하면 스탬프가 추가됩니다(날짜당 여러 개 가능).
- 이전/다음 달 이동 버튼, 상태 안내 문구가 있습니다.
- 별도 백엔드 없이 브라우저 로컬 상태로만 동작합니다.

## Mini Readers 기능 및 API
- 루트(`/`)에서 정적 UI가 서빙되며, 아래 REST API를 호출합니다.
- 요청/응답은 JSON이며, `passage`는 필수, `note`(최대 400자)와 `moods`(0~3개, 각 20자 이내) 중 하나는 반드시 채워야 합니다.

엔드포인트 (기본 프리픽스 `/api/quotes`):
- `GET /` : 최신 순 목록 조회.
- `POST /` : 구절 생성. 요청 예시:
  ```json
  {
    "passage": "마음에 남는 문장",
    "note": "짧은 메모",
    "moods": ["기쁨", "차분"]
  }
  ```
  성공 시 201과 저장된 레코드를 반환합니다.
- `GET /{id}/share` : 공유 카드용 페이로드(`title`, `passage`, `note`, `moods`, `signature`) 반환. 존재하지 않으면 404.

## 문제 해결
- DB가 잠겼다는 메시지가 나오면 애플리케이션을 종료한 뒤 `data/mini-readers.db`를 삭제(또는 백업)하고 다시 실행하세요.
- 포트 충돌 시 `application.properties`에서 `server.port`를 바꾸거나 `--server.port=xxxx`를 실행 옵션에 추가하세요.
