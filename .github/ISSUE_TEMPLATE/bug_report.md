---
name: 🐛 버그 리포트
about: 버그를 발견하셨나요? 상세히 알려주세요!
title: "[BUG] "
labels: bug, needs-triage
assignees: ''
---

## 📋 버그 요약
<!-- 버그를 한 줄로 요약해주세요 -->

## 🎯 영향 범위
- [ ] 🔴 Critical - 서비스 전체 장애 / 데이터 손실
- [ ] 🟠 Major - 주요 기능 동작 불가
- [ ] 🟡 Minor - 일부 기능 오동작 (우회 가능)
- [ ] 🟢 Trivial - UI/UX 이슈, 오타 등

## 📍 영향받는 서비스
<!-- 해당되는 서비스를 모두 체크해주세요 -->
- [ ] 🚪 API Gateway (`api-gateway`)
- [ ] 👤 User Service (`user-service`)
- [ ] 📦 Product Service (`product-service`)
- [ ] 🛒 Order Service (`order-service`)
- [ ] 📨 Kafka (이벤트 처리)
- [ ] 🔧 Common 모듈

## 🐛 버그 상세 설명
<!-- 버그에 대해 명확하고 간결하게 설명해주세요 -->

### 증상
<!-- 어떤 문제가 발생하나요? -->

### 에러 메시지 / 로그
```
<!-- 에러 메시지나 관련 로그를 붙여넣어주세요 -->
```

### 스택 트레이스
<details>
<summary>전체 스택 트레이스 보기</summary>

```java
// 스택 트레이스를 여기에 붙여넣어주세요
```

</details>

## 📋 재현 단계
<!-- 버그를 재현하는 단계를 상세히 작성해주세요 -->

### 사전 조건
<!-- 버그 재현에 필요한 사전 상태 -->
- 예: 사용자가 로그인된 상태
- 예: 특정 상품이 등록되어 있어야 함

### 재현 절차
1. 
2. 
3. 

### 재현 빈도
- [ ] 항상 재현됨 (100%)
- [ ] 자주 재현됨 (50% 이상)
- [ ] 가끔 재현됨 (50% 미만)
- [ ] 한 번만 발생함

### 테스트 데이터 / API 요청
```bash
# 재현에 사용한 API 요청
curl -X POST http://localhost:8080/api/... \
  -H "Content-Type: application/json" \
  -d '{
    "key": "value"
  }'
```

## ✅ 기대 동작
<!-- 정상적으로 동작했을 때 어떤 결과를 기대하셨나요? -->

### 기대한 응답
```json
{
  "success": true,
  "data": { },
  "message": "기대한 메시지"
}
```

## ❌ 실제 동작
<!-- 실제로 어떤 일이 발생했나요? -->

### 실제 응답
```json
{
  "success": false,
  "error": { },
  "message": "실제 에러 메시지"
}
```

## 📸 스크린샷 / 화면 녹화
<!-- 가능하다면 스크린샷이나 화면 녹화를 첨부해주세요 -->
| 기대 동작 | 실제 동작 |
|----------|----------|
|          |          |

## 🔧 환경 정보

### 실행 환경
- **OS**: [예: Windows 11 / macOS 14 / Ubuntu 22.04]
- **Java Version**: [예: 17.0.9]
- **Gradle Version**: [예: 8.5]
- **IDE**: [예: IntelliJ IDEA 2024.1]

### 서비스 버전
- **Spring Boot Version**: [예: 3.2.1]
- **Spring Cloud Version**: [예: 2023.0.0]
- **프로젝트 버전/커밋**: [예: main 브랜치 abc1234]

### 인프라 환경
- **Docker Version**: [예: 24.0.0]
- **Docker Compose Version**: [예: 2.23.0]
- **Kafka Version**: [예: 3.6.0]
- **실행 방식**: 
  - [ ] 로컬 (IDE에서 직접 실행)
  - [ ] Docker Compose
  - [ ] Kubernetes

## 🔍 디버깅 시도

### 이미 시도한 해결 방법
<!-- 문제 해결을 위해 시도한 것들 -->
- [ ] 서비스 재시작
- [ ] Docker 컨테이너 재생성 (`docker-compose down && docker-compose up`)
- [ ] 캐시 삭제 (`./gradlew clean`)
- [ ] 의존성 새로고침
- [ ] 기타: 

### 관련 코드 위치 (알고 있다면)
<!-- 버그와 관련된 코드 파일이나 메서드를 알고 있다면 작성해주세요 -->
```
예: user-service/src/main/java/com/example/userservice/service/UserService.java#L45
```

## 🔗 관련 이슈 / PR
<!-- 관련된 이슈나 PR이 있다면 링크해주세요 -->
- Related to #
- Caused by #
- Blocks #

## 📝 추가 정보

### 임시 해결 방법 (Workaround)
<!-- 임시로 문제를 우회할 수 있는 방법이 있다면 작성해주세요 -->

### 참고 자료
<!-- 관련 문서, 스택오버플로우 링크 등 -->

## ✅ 버그 수정 체크리스트
<!-- 담당자가 버그 수정 시 확인해야 할 사항 -->
- [ ] 버그 원인 분석 완료
- [ ] 수정 코드 작성
- [ ] 버그 재현 테스트 케이스 추가
- [ ] 회귀 테스트 통과 확인
- [ ] 코드 리뷰 완료
- [ ] 문서 업데이트 (필요시)

