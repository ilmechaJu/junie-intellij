# Task 06 — 레거시 모듈 리팩터링 & 회귀 테스트 자동화

task06 문서 활용법
- [Golden-Test-README.md](Golden-Test-README.md) 
  - ci-example.yml 를 만드는 가이드 입니다. 현재 yml파일은 example 형태이며, 실제로 사용할 만한 yml로 만들기 위해서 Junie는 해당문서를 참조해 ci-example.yml 내용을 보강합니다.
- [LEGACY_CODE_DOCUMENTATION.md](LEGACY_CODE_DOCUMENTATION.md)
  - 아래의 5가지 레거시 코드들에 대한 설명입니다. 
    - [InventoryService.java](src/main/java/com/example/legacy/InventoryService.java)
    - [Main.java](src/main/java/com/example/legacy/Main.java)
    - [NotificationService.java](src/main/java/com/example/legacy/NotificationService.java)
    - [OrderProcessor.java](src/main/java/com/example/legacy/OrderProcessor.java)
    - [PaymentService.java](src/main/java/com/example/legacy/PaymentService.java)
    - 위 5가지 레거시 코드들이 
      - 어떤 종류의 레거시 코드인지 설명하는 문서입니다. 
      - 어떤 구조로 의존성 관계가 맺어져 있는지 설명하는 문서입니다. 
      - Refactoring 이후 예상 구조에 관한 내용도 설명하는 문서입니다.


Task06의 목표
- 복잡한 스파게티 코드를 모듈화하고 회귀 방지 테스트 자동화

성공 지표(예)
- 테스트 커버리지: 80%+
- 순환 의존성 0건
- p95 레이턴시 변화 ±5% 이내(회귀 없음)

일정(초안)
- W1: 의존 관계 수집/핫스팟 분석
- W2: 인터페이스 분리/추상화 도입, Golden Test 스냅샷
- W3: 단계적 리팩터링, 호환성 레이어
- W4: CI 구성, 회귀 테스트 안정화

체크리스트(복사/초기화)
- [ ] 문제 정의 및 성공 기준 명시(측정 가능한 지표 포함)
- [ ] ADR 1건 이상 작성 및 대안 비교
- [ ] 코드 변경에 대한 단위/통합/필요 시 e2e 테스트
- [ ] CI 파이프라인에서 빌드/테스트/린트 통과
- [ ] 보안/품질 스캔(가능 시) 수행 및 결과 기록
- [ ] 성능 영향(있다면) 측정 전/후 비교 공개
- [ ] 릴리즈 노트 작성 및 롤백 절차 문서화

Task 06 전용 체크리스트
- [ ] 순환 의존 제거, 사이드이펙트 캡슐화, public API 축소/정리
- [ ] Golden Test로 기존 동작 스냅샷 확보
- [ ] Deprecated shim 및 호환성 레이어 제공(필요시)

산출물
- /task06/diagrams/: 변경 전/후 의존성 다이어그램
- /task06/tests/: 단위/통합/Golden 테스트
- /task06/ci/: 회귀 테스트 워크플로우(yml 예시)
