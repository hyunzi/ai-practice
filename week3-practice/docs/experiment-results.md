# DevDocs RAG 실험 로그

목표: chunk 크기(300 vs 600)와 top-k(3/5) 조합에 따른 검색 품질과 응답 품질을 비교하는 실험 계획 메모입니다. 실제 수치는 아직 기록되지 않았습니다.

## 체크리스트
- [ ] `POST /api/devdocs/ingest`로 `size300`, `size600` 컬렉션 각각 생성
- [ ] `POST /api/devdocs/query`로 `mode=size300|size600` 및 top-k 3/5 비교
- [ ] DEBUG 로그(`rag_debug=...`)로 매치된 chunk/score 확인
- [ ] 동일 질문 세트로 응답 품질 주관 평가 기록

## 샘플 질문
- "How do I update a stored payment method in Stripe?"
- "What webhook events do I need for payouts?"
- "How do I test decline codes in Stripe?"

## 결과 기록 양식 (예시)
| 설정 | 질문 | top-k | 관찰/메모 |
|------|------|-------|-----------|
| size300 | How do I update a stored payment method in Stripe? | 3 | (관찰 결과를 여기에 적으세요) |
| size600 | How do I update a stored payment method in Stripe? | 3 | (관찰 결과를 여기에 적으세요) |
| size300 | … | 5 | … |
| size600 | … | 5 | … |

> 위 표를 채워가며, 어떤 조합이 더 관련도 높은 chunk를 반환하는지 메모해 두세요.
