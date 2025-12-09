# Dev Docs RAG 실험 결과 로그

## 1. 실험 개요
- 목적: 청크 크기(300 vs 600)와 top-k가 검색 품질에 미치는 영향 확인
- 모델: Chat `gpt-4o-mini`, Embedding `text-embedding-3-small`
- 스토어: Chroma(없을 시 인메모리)
- 데이터: `data/devdocs/stripe/**/*.md`

## 2. 체크리스트
- [ ] `POST /api/devdocs/ingest` 수행 후 컬렉션 확인 (`/api/devdocs/collections`)
- [ ] `size300` / `size600` 각각 동일 질의로 RAG 실행
- [ ] top-k 3/5 비교(응답 일관성, 관련성)
- [ ] `rag_debug` 로그로 매치 프리뷰 및 점수 확인

## 3. 테스트 질문 예시
- "How do I update a stored payment method in Stripe?"
- "What webhook events do I need for payouts?"
- "How do I test decline codes in Stripe?"

## 4. 결과 스냅샷 (작성 시 갱신)
| 설정 | 질문 | top-k | 관찰 |
|------|------|-------|------|
| size300 | - | 3 | (미측정) |
| size600 | - | 3 | (미측정) |

> 아직 실험을 수행하지 않았습니다. 위 표에 질문/설정/관찰을 채워 넣어 로그를 축적하세요.
