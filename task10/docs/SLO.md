# SLO (Service Level Objective)

서비스: Task10 Todo 백엔드

SLO 타겟
- 가용성: 월간 99.9% (오류율 0.1% 이하)
- 지연: p95 요청 지연 1초 이하 (업무시간 09:00-18:00 기준)

측정 방법
- 메트릭
  - `http_requests_total{service="task10-backend",code}` → 오류율 계산
  - `http_request_duration_seconds_bucket{service="task10-backend"}` → p95 계산
- Prometheus 규칙
  - alerts.yml의 `Task10HighErrorRate`, `Task10HighLatency`

에러 예산 관리
- 월간 에러 예산: 0.1%
- 초과 시: 변경 동결(Freeze), 원인분석(RCA), 성능 개선 항목 우선순위 상향

대시보드
- 패널
  - p50/p95/p99 레이턴시
  - 에러율(5xx 비율)
  - RPS, 인스턴스 헬스(헬스체크), 자원지표(선택)
