# 운영 핸드북 (Runbook)

서비스: Task10 Todo 백엔드

연락/온콜
- 소유팀: team-app
- 온콜: Slack #oncall-app, 전화 XXX-XXXX-XXXX

헬스 체크
- HTTP: `GET /healthz` → `{ ok: true }`
- 메트릭: `GET /metrics`

일상 점검 체크리스트
- 에러율(5xx) 5분 이동평균 < 1%
- p95 지연 < 1s (업무시간 기준)
- 최근 배포/마이그레이션 성공 여부 확인

장애 대응
1) 알림 확인 (Prometheus/Alertmanager)
2) 영향 범위 파악: 에러율/레이턴시/요청수, 최근 변경 내역
3) 완화 조치
   - 트래픽 급증: 스케일 아웃(해당 인프라 정책에 따름)
   - 성능 저하: 로그/프로파일링, 캐시/쿼리 최적화
   - 5xx 급증: 최근 배포 롤백 고려
4) 커뮤니케이션: 상태 채널에 현황 공유, ETA

배포
- 사전: 변경 요약/리스크/롤백 플랜 작성
- 절차:
  1) 코드 배포 (앱은 컬럼 유무를 허용)
  2) 마이그레이션 적용: `cd backend && npm run migrate`
  3) 선택: 백필 실행 `npm run backfill`
  4) 검증: `/healthz`, 기능 점검, `/metrics`
- 실패 시: 롤백 절차 수행

롤백
- 최근 마이그레이션 롤백: `cd backend && npm run rollback`
- 앱은 `due_date` 컬럼이 없더라도 동작해야 함(방어 로직 포함)

관측성
- 주요 지표: `http_requests_total`, `http_request_duration_seconds`, `process_*`
- 대시보드: SLO 문서의 패널 가이드 참고
- 알림: `observability/alerts.yml`

보안/컴플라이언스
- 입력 검증: 제목 필수, JSON Content-Type 필요
- 로그: PII 저장 금지, 접근 권한 통제

런북 변경 관리
- 변경 시 PR로 리뷰/승인 후 병합
