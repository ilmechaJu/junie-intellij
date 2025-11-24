# 릴리즈 노트 — Task10 Todo 마감일 기능

버전: 1.0.0
날짜: 2025-11-24

요약
- Todo에 `마감일(due_date)` 필드를 도입하고 UI/백엔드/관측성을 연계했습니다.

변경 사항
- 백엔드
  - `/api/todos` 목록/생성, `/api/todos/:id/complete` 완료 API 제공
  - `due_date` 컬럼(Nullable) 추가 (마이그레이션 002)
  - Prometheus 메트릭 `/metrics` 제공 (기본 프로세스 + 요청 지연 히스토그램 + 요청 카운터)
- 프론트엔드
  - 목록/생성/완료 UI 제공, `마감일` 입력 지원
- 관측성
  - Prometheus 스크랩 설정 예시, Alert 규칙 예시, SLO 문서 제공

호환성/사용자 영향
- 기존 데이터: `due_date`는 NULL 허용이며 백필 스크립트로 기본값을 채울 수 있습니다 (created_at + 7일)
- API 호환성: `dueDate` 필드는 응답에 Optional로 포함됩니다. 클라이언트는 필드 부재/NULL을 허용해야 합니다.

업그레이드 가이드
1) 애플리케이션 코드 배포(기존 DB에서도 동작)
2) 마이그레이션 적용: `cd backend && npm run migrate`
3) 선택: 데이터 백필 실행 `npm run backfill`
4) 검증: `/healthz`, 기능 점검, `/metrics` 확인

롤백 가이드
- 최근 마이그레이션 롤백: `npm run rollback`
- 주의: `due_date` 컬럼이 제거될 수 있으므로, 클라이언트는 해당 필드 미존재를 허용해야 합니다.

알려진 이슈
- 대량 데이터 백필 시 시간이 소요될 수 있습니다. 배치 처리 권장.
