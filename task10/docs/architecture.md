# 아키텍처 개요 (Task 10)

목표 기능
- 할 일(Todo) 관리에 `마감일(due_date)`을 추가하는 신규 기능을 E2E로 제공
- 데이터 모델 → 백엔드 API → 프론트(UI) → 관측성(메트릭/알림) → 릴리즈/롤백까지 포함

구성 요소
- Backend: Node.js(Express) + SQLite(better-sqlite3)
  - REST API: 목록, 생성, 완료 처리
  - 마이그레이션/백필 스크립트 포함
  - /metrics(프로메테우스), /healthz 헬스 체크 제공
  - 정적 프론트엔드(backend/public) 서빙
- Frontend: 정적 HTML/CSS/JS (fetch 기반)
- Observability: Prometheus 스크랩 설정, Alert Rule 예시, Grafana 대시보드 초안

데이터 모델
- 테이블: `todos`
  - `id` (INTEGER PK)
  - `title` (TEXT, NOT NULL)
  - `completed` (INTEGER, 0/1)
  - `created_at`, `updated_at` (TEXT, ISO8601)
  - `due_date` (TEXT, NULLABLE; 2단계에서 추가)

릴리즈 전략 (Zero-downtime)
1) 애플리케이션이 `due_date` 컬럼 유무에 관계없이 동작하도록 코드 배포
2) 마이그레이션으로 `due_date` 컬럼 추가 (NULL 허용)
3) 백필 스크립트로 기존 행에 기본 마감일 채우기(선택/비동기)
4) 기능/UI/대시보드에서 `due_date` 활용
5) 필요시 후속 마이그레이션으로 제약 강화(NOT NULL 등)

에러/성능 관측 지표
- `http_request_duration_seconds` 히스토그램(p95 레이턴시)
- `process_*` 기본 메트릭
- 에러율(5xx 비율) 알림 룰 예시 제공
