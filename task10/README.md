# Task 10 — E2E 기능제공: 데이터모델→백엔드→프론트→관측성→릴리즈

목표
- 현실적인 신규 기능을 설계부터 배포까지 E2E로 제공 (Todo에 마감일 기능)

체크리스트 (이 저장소 기준 상태)
- [x] 장애 없는 마이그레이션/롤백 검증, 데이터 백필 스크립트
- [x] 품질 기준(SLO) 정의 및 준수 가이드
- [x] 사용자 영향/호환성 주의가 릴리즈 노트에 명확

디렉토리
- /task10/backend/ (Express + SQLite, 정적 프론트 포함)
- /task10/observability/ (Prometheus 설정/Alert 예시)
- /task10/docs/ (아키텍처, SLO, 마이그레이션, 릴리즈 노트, 런북)

빠른 시작 (로컬)
1) 백엔드 실행
   - `cd backend && npm i && npm run migrate && npm start`
   - 브라우저: http://localhost:3000
   - 헬스: http://localhost:3000/healthz
   - 메트릭: http://localhost:3000/metrics
2) 기능 확인
   - 메인 화면에서 Todo 생성/완료, 마감일 입력/표시 확인
3) 관측성 확인 (옵션)
   - Prometheus: `observability/prometheus.yml` 사용
   - Docker 예시(Windows PowerShell):
     - `docker run --rm -p 9090:9090 -v ${PWD}/observability/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus`
   - 쿼리 예: 오류율/지연은 docs/SLO.md 참고

데이터 모델과 마이그레이션
- 초기 테이블: `001_init.sql`
- 마감일 컬럼 추가: `002_add_due_date.sql` (NULL 허용)
- 백필 스크립트: `npm run backfill` (NULL인 due_date를 created_at+7일로 채움)
- 롤백: `npm run rollback` (가장 최근 배치 되돌림)

배포/롤백 절차
- 코드 선배포 → 마이그레이션 적용 → (선택) 백필 → 검증
- 상세는 `docs/runbook.md`, `docs/migration.md`, `docs/release-notes.md` 참조

프론트엔드
- 정적 파일은 `backend/public/`에 위치하며 백엔드가 서빙합니다
- 별도 빌드 과정 없음

참고 문서
- docs/architecture.md — 전체 구성 및 데이터 모델
- docs/SLO.md — 품질 기준 및 측정 방법
- docs/migration.md — Zero-downtime 마이그레이션 계획
- docs/release-notes.md — 사용자 영향/호환성 포함 릴리즈 노트
- docs/runbook.md — 운영 핸드북
