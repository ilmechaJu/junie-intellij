# Task 07 — Contract-first API(서버/클라이언트/계약 테스트)

목표
- OpenAPI 스펙을 기준으로 서버 스텁, 타입 안전 SDK, 계약 테스트 구축

체크리스트
- [x] 스펙과 구현 drift 0
- [x] 표준화된 에러 응답과 재시도 정책 정의
- [x] 계약 테스트 CI 통과 및 샌드박스 예제 동작

디렉토리
- /task07/spec/
- /task07/server/
- /task07/sdk/
- /task07/tests-contract/

구성 요약
- 스펙: `/task07/spec/openapi.yaml` — `GET /health` 및 RFC7807 `ProblemDetails`, `ServerError` 응답, `x-request-id` 헤더 정의
- 서버: Express + `express-openapi-validator` — 스펙 기반 요청/응답 검증, `/health` 구현, RFC7807 에러 처리, `x-request-id` 헤더 세팅
- SDK: 타입스크립트 클라이언트 — `getHealth()` 제공, 타임아웃/재시도(5xx)/백오프 지원, `ProblemDetails` 매핑
- 계약 테스트: 스펙 유효성 검사 + 서버 응답 스키마/헤더 확인(드리프트 0 보장)

실행 방법 (Windows PowerShell 기준)
1) 서버
- 설치: `cd task07/server; npm i`
- 개발 실행(핫리로드): `npm run dev`
- 빌드 후 실행: `npm run build && npm start`

2) SDK 샌드박스
- 설치/빌드: `cd task07/sdk; npm i && npm run build`
- 예제 실행(서버가 떠 있어야 함): `npm run dev`

3) 계약 테스트
- 설치: `cd task07/tests-contract; npm i`
- 실행: `npm test`
  - 수행 내용: OpenAPI 스펙 스키마 검증 → 서버 앱 인메모리 기동 → `/health` 200 응답의 바디/헤더 스펙 검증 → 404 시 `ProblemDetails` 형태 검증

스펙 변경 시 가이드
- `/task07/spec/openapi.yaml` 수정 → 서버/SDK가 스펙과 동기화되는지 `tests-contract`로 검증
- 표준 에러/헤더는 `components` 재사용(ProblemDetails, ServerError, x-request-id)
