# profile

이 디렉토리는 CPU/메모리 프로파일 및 분석 스냅샷(스크린샷, 텍스트 보고서)을 보관합니다.

권장 툴
- Chrome DevTools (Node 프로파일/힙 스냅샷 열람)
- speedscope.app (cpuprofile 시각화)

프로파일 캡처 (Node.js)
1) CPU 프로파일
- PowerShell 예시:
```
node --expose-gc --cpu-prof --cpu-prof-dir .\profile .\bench\algo-bench.js --n=200000 --k=25 --it=30
```
- 실행이 끝나면 `.\profile\isolate-*.cpuprofile` 파일이 생성됩니다.
- 열람: Chrome DevTools(Performance 탭) 또는 speedscope.app 에 파일을 드롭.

2) 힙(메모리) 분석
- 간단히 RSS 피크는 벤치 결과로 확인 가능.
- 정밀 분석을 원하면 DevTools 메모리 탭 사용:
  - `node --inspect .\bench\algo-bench.js` 로 실행
  - Chrome에서 `chrome://inspect` → 해당 프로세스 `inspect`
  - Memory 탭에서 Heap snapshot 캡처 후 `.\profile` 폴더에 내보내기

권장 보관물
- `cpu-before.cpuprofile`, `cpu-after.cpuprofile`
- `heap-before.heapsnapshot`, `heap-after.heapsnapshot`
- 주요 스크린샷(플레임그래프, 할당 경로)과 간단한 해설 `REPORT.md`

보고서 템플릿(`REPORT.md` 예시)
```
# 프로파일링 보고서

## 환경
- Node: vXX
- OS/CPU: Windows 10 / x64
- 파라미터: --n=200000 --k=25 --it=30

## CPU
- 이전(p95/p99): ...ms / ...ms
- 이후(p95/p99): ...ms / ...ms
- 핫패스: Map count + sort → 개선: 빈도 버킷 순회

## 메모리
- 이전 피크 RSS: ... MB
- 이후 피크 RSS: ... MB
- 메모리 핫스팟/누수 징후: 없음/의심 지점 ...

## 결론
- p95 개선: ..%, p99 개선: ..%, 피크 RSS 개선: ..%
```


---


