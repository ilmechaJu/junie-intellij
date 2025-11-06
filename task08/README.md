# Task 08 — 성능 병목 탐지, 프로파일링, 알고리즘 개선

이 저장소는 “병목 탐지 → 프로파일링 → 알고리즘/메모리 개선 → 재검증”까지 한 번에 수행할 수 있도록 준비된 실습 키트입니다.
Node.js(벤치/프로파일)와 k6(부하 테스트)를 사용합니다.

초보자도 복붙으로 바로 따라 할 수 있도록, 명령은 모두 Windows PowerShell 기준으로 제공합니다.


## 목표(What you’ll achieve)
- p95/p99 지연시간을 개선하고, 평균뿐 아니라 분포의 꼬리도 좋아졌음을 증빙
- 피크 메모리(RSS) 사용량 감소 또는 회귀 없음 확인
- 전/후 결과를 JSON/JFR/프로파일 파일로 보관하여 재현성 확보


## 체크리스트
- [ ] p95/p99 레이턴시 개선 달성(벤치 또는 k6 결과 근거)
- [ ] 피크 메모리(RSS) 감소, 회귀 없음
- [ ] 결과 산출물 보관: `bench/results/*.json`, `profile/*.cpuprofile|*.heapsnapshot|*.jfr`, `profile/REPORT.md`


## 디렉토리 구조
- `bench/`        마이크로벤치마크(Node): 알고리즘 전/후(p95/p99/메모리) 비교, JSON 자동 저장
- `profile/`      프로파일 산출물 보관 폴더(.cpuprofile/.heapsnapshot) + 보고서 템플릿 가이드
- `scripts/`      k6 부하 테스트 스크립트(임계값 내장)


자세한 사용법은 각 폴더의 `README.md`에도 있습니다.


## 사전 준비(Prerequisites)
- Node.js 16+ (LTS 권장)
- k6 (부하 테스트 도구)
  - Windows (권장 순)
    1) winget: `winget install grafana.k6`
    2) Chocolatey: `choco install k6 -y`
    3) MSI 설치 파일: https://github.com/grafana/k6/releases 에서 최신 `k6-vX.Y.Z-windows-amd64.msi` 다운로드 후 설치 → PowerShell 재시작
- Chrome DevTools 또는 https://www.speedscope.app (CPU/Heap 분석용)


설치 확인(Windows PowerShell)
```
k6 version
# 문제가 있으면 PATH 확인
Get-Command k6
```


## 5분 빠른 시작(권장 흐름 요약)
먼저 `task08` 디렉토리로 이동한 뒤, 아래 명령을 순서대로 실행하세요.

```
cd .\task08
```

1) 기준선 벤치마크 실행(Node)
```
node --expose-gc .\bench\algo-bench.js --n=200000 --k=25 --it=30
```
- 콘솔 요약: p95/p99, mean, peakRssMB, 개선율(naive vs optimized)
- JSON 저장: `bench/results/result-<timestamp>.json`

2) CPU 프로파일 캡처(Node)
```
node --expose-gc --cpu-prof --cpu-prof-dir .\profile .\bench\algo-bench.js --n=200000 --k=25 --it=30
```
- 산출: `profile/isolate-*.cpuprofile` → DevTools 또는 speedscope로 열람

3) 부하 테스트(k6) 스모크
```
k6 run .\scripts\k6-smoke.js
```
- 고정 도착률 예시(50rps, 60s)
```
$Env:BASE_URL="http://localhost:8080"; $Env:PATH="/health"; $Env:RATE="50"; $Env:DURATION="60s"; k6 run .\scripts\k6-smoke.js
```
- 참고: 대상 서비스가 기동 중이어야 합니다. 서비스가 없으면 실패율이 100%로 나옵니다.
  - 빠른 대안: Docker가 있다면 `docker run --rm -p 8080:5678 hashicorp/http-echo -text="OK"` 후 `-e BASE_URL`/`-e PATH`로 실행하거나,
  - 실제 서비스 URL을 `BASE_URL`/`PATH`로 지정하세요. PowerShell 환경변수 대신 `k6 run -e ...` 사용을 권장합니다.

4) 결과 정리
- JSON/JFR/프로파일 파일을 `bench/results/`, `profile/`에 보관
- `profile/REPORT.md` 템플릿에 수치와 캡처를 붙여 요약 작성

참고: 만약 저장소 루트에서 실행하고 싶다면, 모든 경로 앞에 `task08\` 프리픽스를 붙이면 됩니다. 예) `node .\task08\bench\algo-bench.js ...`



비교/검증 포인트
- p95/p99, mean: 전/후 개선율(%)
- peakRssMB: 메모리 피크 감소 여부
- k6 임계값 충족 여부: 실패율 <1%, p95/p99 기준 통과


## 자주 묻는 질문(FAQ)
- 본 키트는 Node 중심이며, 서비스가 Java라도 k6는 그대로 사용 가능합니다.
- 경로가 `task08\...`와 `bench\...`로 섞여 보이는데요?
  - 기본은 `cd .\\task08` 후 `bench\...`처럼 사용하세요. 루트에서 실행하려면 앞에 `task08\`만 붙이면 됩니다.


## 참고 문서
- `bench/README.md` — 벤치 사용법/해석
- `profile/README.md` — 프로파일 캡처/분석 및 REPORT 템플릿
- `scripts/README.md` — k6 사용법과 옵션

