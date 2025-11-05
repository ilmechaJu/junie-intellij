# Task 07 — Contract-first API(서버/클라이언트/계약 테스트)

목표
- OpenAPI 스펙을 기준으로 서버 스텁, 타입 안전 SDK, 계약 테스트 구축

체크리스트
- [ ] 스펙과 구현 drift 0
- [ ] 표준화된 에러 응답과 재시도 정책 정의
- [ ] 계약 테스트 CI 통과 및 샌드박스 예제 동작

디렉토리
- /task07/spec/
- /task07/server/
- /task07/sdk/
- /task07/tests-contract/

시작하기
- spec/openapi.yaml에 엔드포인트/스키마 추가
- 서버/SDK 스텁 생성 후 계약 테스트를 spec 기반으로 작성
