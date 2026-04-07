## 📋 PR 요약
<!-- 이 PR에서 변경한 내용을 간략히 설명해주세요 -->

## 🔗 관련 Issue
<!-- 관련된 Issue가 있다면 링크해주세요 -->
- Closes #

## 🔄 변경 사항
<!-- 주요 변경 사항을 나열해주세요 -->
- 
- 
- 

## 📝 변경 유형
- [ ] 🐛 버그 수정 (기존 기능을 수정하는 non-breaking change)
- [ ] ✨ 새 기능 (기존 기능을 수정하지 않는 non-breaking change)
- [ ] 💥 Breaking change (기존 기능에 영향을 주는 수정)
- [ ] 📚 문서 업데이트
- [ ] 🔧 설정 변경
- [ ] ♻️ 리팩토링

## ✅ 체크리스트

### 🎨 코드 스타일 & 포맷팅
- [ ] 코드 포맷터를 적용했습니다 (`./gradlew spotlessApply` 또는 IDE 포맷팅)
- [ ] 린터 검사를 통과했습니다 (`./gradlew checkstyleMain`)
- [ ] 불필요한 import 문을 제거했습니다
- [ ] 클래스/메서드/변수 네이밍이 컨벤션을 따릅니다 (PascalCase, camelCase, UPPER_SNAKE_CASE)
- [ ] 하드코딩된 값 없이 상수 또는 설정을 사용했습니다

### 🧪 테스트
- [ ] 새로운 기능에 대한 단위 테스트를 추가했습니다
- [ ] 테스트 메서드명이 `should_기대결과_when_조건` 형식을 따릅니다
- [ ] 모든 단위 테스트가 통과합니다 (`./gradlew test`)
- [ ] 테스트 커버리지가 기존 수준을 유지하거나 향상되었습니다
- [ ] 엣지 케이스와 예외 상황에 대한 테스트를 포함했습니다
- [ ] Mock 객체를 적절히 사용했습니다 (Mockito)

### 📚 문서화
- [ ] 새로운 API에 대한 Javadoc 주석을 추가했습니다
- [ ] README 또는 관련 문서를 업데이트했습니다 (필요한 경우)
- [ ] API 변경 시 요청/응답 예시를 문서화했습니다
- [ ] 복잡한 비즈니스 로직에 대한 주석을 추가했습니다

### 🔒 보안 & 품질
- [ ] 민감 정보(비밀번호, API 키 등)가 코드에 포함되지 않았습니다
- [ ] 입력값 검증 로직이 포함되어 있습니다 (`@Valid`, `@NotBlank` 등)
- [ ] 에러 응답이 `ApiResponse` 형식을 따릅니다
- [ ] 로그 레벨이 적절히 설정되어 있습니다 (info, warn, error)

### 📋 기타
- [ ] PR 제목이 [커밋 컨벤션](https://www.conventionalcommits.org/)을 따릅니다
- [ ] 불필요한 디버깅 코드 및 console.log를 제거했습니다
- [ ] 빌드가 성공적으로 완료됩니다 (`./gradlew build`)

## 📸 스크린샷 (UI 변경 시)
<!-- UI 변경이 있다면 before/after 스크린샷을 첨부해주세요 -->

## 🧪 테스트 방법
<!-- 이 PR을 테스트하는 방법을 설명해주세요 -->

### 로컬 테스트
```bash
# 1. 전체 빌드 및 테스트
./gradlew clean build

# 2. 단위 테스트만 실행
./gradlew test

# 3. 특정 서비스 테스트
./gradlew :user-service:test
./gradlew :order-service:test
./gradlew :product-service:test

# 4. 코드 스타일 검사
./gradlew checkstyleMain
./gradlew spotlessCheck
```

### 통합 테스트 (Docker)
```bash
# 1. 서비스 실행
docker-compose up -d

# 2. API 테스트
curl http://localhost:8081/api/users
curl http://localhost:8082/api/products
curl http://localhost:8083/api/orders

# 3. Kafka 이벤트 확인
# Kafka UI: http://localhost:8090
```

### 테스트 결과
<!-- 테스트 실행 결과를 첨부해주세요 -->
```
./gradlew test 실행 결과:
- UserServiceTest: ✅ PASSED (X tests)
- OrderServiceTest: ✅ PASSED (X tests)
- ProductServiceTest: ✅ PASSED (X tests)
```

## 💬 추가 코멘트
<!-- 리뷰어에게 전달할 추가 정보가 있다면 작성해주세요 -->

