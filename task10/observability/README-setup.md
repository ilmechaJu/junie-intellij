# Observability Setup (Prometheus/Grafana)

구성 요소
- Backend: `/metrics` 에 Prometheus 포맷 노출 (기본 프로세스 + 커스텀 지표)
  - `http_request_duration_seconds` (Histogram)
  - `http_requests_total` (Counter)
- Prometheus: `observability/prometheus.yml` 예시 사용
- Alert: `observability/alerts.yml` 예시 사용
- Grafana: 대시보드 패널은 SLO 문서 참고하여 생성

로컬 실행 예시
1) 백엔드 기동
   - `cd backend && npm i && npm start`
   - 메트릭: http://localhost:3000/metrics
2) Prometheus 실행 (docker)
   - `docker run --rm -p 9090:9090 -v %cd%/observability/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus`
   - Windows PowerShell에서 실행 시 경로 확인 필요. (예: 절대 경로 사용)
3) Alertmanager 연동은 별도 구성 필요 (옵션)

SLO 모니터링
- Prometheus에서 다음 쿼리 활용
  - 오류율: `sum(rate(http_requests_total{service="task10-backend",code=~"5.."}[5m])) / sum(rate(http_requests_total{service="task10-backend"}[5m]))`
  - p95 지연: `histogram_quantile(0.95, sum(rate(http_request_duration_seconds_bucket{service="task10-backend"}[5m])) by (le))`

운영 팁
- 스크랩 주기/평가 주기는 워크로드에 맞게 조정
- 경계값 튜닝: 베이스라인 관측 후 임계값을 단계적으로 조정
