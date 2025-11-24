# scripts

이 디렉토리에는 로드/스트레스 테스트 스크립트를 둡니다.

예시 도구
- k6: 자바스크립트 기반 HTTP 부하 테스트
- Apache JMeter: GUI/CLI 로드 테스트

## k6 스크립트
- `k6-smoke.js`: 기본 헬스엔드포인트 대상 스모크/부하 테스트
  - 환경 변수로 설정 가능: `BASE_URL`, `PATH`, `VUS`, `DURATION`, `RATE`
  - `RATE`가 0이면 단순 VU 지속 시나리오, `RATE>0`이면 constant-arrival-rate 모드
  - 기본 임계값(threshold): `http_req_duration{p95<500ms, p99<1000ms}`, 실패율 `<1%`

PowerShell 실행 예시
```
# 스모크 테스트 (기본 10 VUs, 30초)
k6 run .\scripts\k6-smoke.js

# p95/p99 목표를 확인하면서 50 rps 고정 도착률, 60초
$Env:BASE_URL="http://localhost:8080"; $Env:PATH="/health"; $Env:RATE="50"; $Env:DURATION="60s"; k6 run .\scripts\k6-smoke.js

# 20 VUs, 45초(도착률 미지정)
$Env:VUS="20"; $Env:DURATION="45s"; k6 run .\scripts\k6-smoke.js
```

참고
- k6 설치: https://k6.io
- 결과 요약에서 p95/p99를 추출하여 `profile/REPORT.md` 또는 벤치 결과와 함께 비교 기록하세요.


---

## 실행 전 필수 조건(중요)
- 대상 서비스가 기동 중이어야 합니다. 기본 대상은 `http://localhost:8080/health`입니다.
- 서비스가 없으면 요청이 100% 실패로 기록되는 것이 정상입니다. 아래 대안을 참고하세요.
- PowerShell에서 시스템 `$Env:PATH`를 오염하지 않도록, 환경변수는 가급적 k6의 `-e` 옵션으로 전달하세요.

## 빠른 실행 예시(-e 권장)
```
# 스모크(기본값 사용)
k6 run -e BASE_URL=http://localhost:8080 -e PATH=/health ./scripts/k6-smoke.js

# 50 rps, 60초(임계값 확인)
k6 run -e BASE_URL=http://localhost:8080 -e PATH=/health -e RATE=50 -e DURATION=60s ./scripts/k6-smoke.js
```

## 대상 서비스가 없을 때(대안)
```
# Docker가 있다면, 즉석 에코 서버 기동(포트 8080 이용)
docker run --rm -p 8080:5678 hashicorp/http-echo -text="OK"
# 그 후
a) 루트에서:    k6 run -e BASE_URL=http://localhost:8080 -e PATH=/ ./scripts/k6-smoke.js
b) cd task08 후: k6 run -e BASE_URL=http://localhost:8080 -e PATH=/ ./scripts/k6-smoke.js
```
- 또는 실제 서비스의 URL/경로를 `-e BASE_URL`, `-e PATH`에 지정하세요.

## 설치 참고
- k6 설치 방법은 루트 `README.md`의 "사전 준비(Prerequisites)" 섹션을 참고하세요.
