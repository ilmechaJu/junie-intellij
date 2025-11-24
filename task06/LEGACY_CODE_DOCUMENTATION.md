# 레거시 코드 문서화

## 개요

이 문서는 Task06의 리팩터링 대상인 레거시 코드의 구조, 문제점, 의존성 관계를 설명합니다.

## 프로젝트 구조

```
src/main/java/com/example/legacy/
├── Main.java                  # 애플리케이션 진입점
├── OrderProcessor.java        # 주문 처리 메인 클래스 (거대한 스파게티 코드)
├── PaymentService.java        # 결제 처리 서비스 (순환 의존성)
├── InventoryService.java      # 재고 관리 서비스 (순환 의존성, 전역 상태)
└── NotificationService.java   # 알림 서비스 (전역 상태)
```

## 클래스 상세 설명

### 1. Main.java

**역할**: 애플리케이션의 진입점

**설명**:
- 간단한 메인 메서드로 `OrderProcessor`를 생성하고 주문을 처리합니다.
- 하드코딩된 값으로 주문을 실행합니다.

**코드 예시**:
```java
OrderProcessor processor = new OrderProcessor();
processor.processOrder("ORDER-001", "USER-123", 100.0);
```

---

### 2. OrderProcessor.java

**역할**: 주문 처리의 모든 로직을 담당하는 거대한 클래스

**주요 기능**:
- 주문 검증
- 재고 확인
- 결제 처리
- 재고 업데이트
- 알림 전송
- 전역 상태 관리

**문제점**:
1. **단일 책임 원칙 위반**: 하나의 클래스가 너무 많은 책임을 가짐
2. **강한 결합**: 모든 서비스를 직접 생성하고 의존
3. **전역 상태**: 정적 필드로 상태 관리 (`totalOrders`, `processedOrders`)
4. **순환 의존성**: 다른 서비스들과 서로 참조
5. **테스트 어려움**: 의존성 주입 불가, 전역 상태 사용
6. **방어적 복사 없음**: 내부 리스트를 직접 반환

**주요 메서드**:
- `processOrder(String orderId, String userId, double amount)`: 주문 처리의 모든 로직이 포함된 거대한 메서드
- `onPaymentFailed(String orderId)`: 결제 실패 콜백 (순환 의존성)
- `onInventoryLow(String itemId)`: 재고 부족 콜백 (순환 의존성)

**의존성**:
- `PaymentService` (직접 생성)
- `InventoryService` (직접 생성)
- `NotificationService` (직접 생성)

---

### 3. PaymentService.java

**역할**: 결제 처리 담당

**주요 기능**:
- 결제 처리 (`processPayment`)
- 환불 처리 (`refundPayment`)

**문제점**:
1. **순환 의존성**: `OrderProcessor`와 서로 참조
   - `OrderProcessor`가 `PaymentService`를 생성
   - `PaymentService`가 `OrderProcessor`를 참조
2. **하드코딩된 로직**: 1000원 이상 금액은 무조건 실패 처리
3. **테스트 어려움**: `OrderProcessor`에 의존하여 독립 테스트 불가

**의존성**:
- `OrderProcessor` (순환 참조)

**순환 의존성 흐름**:
```
OrderProcessor → PaymentService → OrderProcessor (순환!)
```

---

### 4. InventoryService.java

**역할**: 재고 관리 담당

**주요 기능**:
- 재고 확인 (`checkStock`)
- 재고 업데이트 (`updateStock`)

**문제점**:
1. **순환 의존성**: `OrderProcessor`와 서로 참조
2. **전역 상태**: 정적 필드 `stock`으로 재고 관리
3. **방어적 복사 없음**: `getStock()`이 내부 맵을 직접 반환
4. **사이드 이펙트**: 재고 업데이트 시 자동으로 알림 전송

**의존성**:
- `OrderProcessor` (순환 참조)

**전역 상태**:
```java
private static Map<String, Integer> stock = new HashMap<>();
```

**순환 의존성 흐름**:
```
OrderProcessor → InventoryService → OrderProcessor (순환!)
```

---

### 5. NotificationService.java

**역할**: 알림 전송 담당

**주요 기능**:
- 알림 전송 (`sendNotification`)

**문제점**:
1. **전역 상태**: 정적 필드 `notifications`로 모든 알림 저장
2. **하드코딩된 출력**: `System.out.println` 사용 (로깅과 섞임)
3. **테스트 어려움**: 의존성 주입 불가, 전역 상태 사용
4. **방어적 복사 없음**: `getNotifications()`가 내부 리스트를 직접 반환

**전역 상태**:
```java
private static List<String> notifications = new ArrayList<>();
```

---

## 의존성 관계도

### 현재 구조 (순환 의존성 포함)

```
┌─────────────────┐
│ OrderProcessor  │
│  (거대 클래스)   │
└────────┬────────┘
         │
         ├─────────────────┬─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
┌─────────────────┐ ┌──────────────┐ ┌─────────────────┐
│ PaymentService  │ │InventoryService│ │NotificationService│
│ (순환 의존)     │ │ (순환 의존)    │ │ (전역 상태)      │
└────────┬────────┘ └───────┬───────┘ └─────────────────┘
         │                  │
         └──────────┬───────┘
                    │
                    ▼
            ┌───────────────┐
            │ 순환 의존성!   │
            └───────────────┘
```

### 문제점 시각화

```
OrderProcessor
    ↓ 생성
PaymentService
    ↓ setOrderProcessor()
OrderProcessor
    ↑ (순환!)
```

## 발견된 문제점 요약

### 1. 순환 의존성 (Circular Dependency)

**위치**: `OrderProcessor` ↔ `PaymentService` ↔ `InventoryService`

**영향**:
- 컴파일 순서에 의존
- 테스트 어려움
- 모듈 분리 불가
- 유지보수 어려움

**예시**:
```java
// OrderProcessor.java
this.paymentService = new PaymentService();
paymentService.setOrderProcessor(this); // 순환 참조!

// PaymentService.java
private OrderProcessor orderProcessor; // 역순환 참조!
```

---

### 2. 전역 상태 (Global State)

**위치**:
- `OrderProcessor.totalOrders` (정적 필드)
- `OrderProcessor.processedOrders` (정적 필드)
- `InventoryService.stock` (정적 필드)
- `NotificationService.notifications` (정적 필드)

**영향**:
- 테스트 간 간섭
- 멀티스레드 환경에서 위험
- 예측 불가능한 동작
- 디버깅 어려움

**예시**:
```java
// OrderProcessor.java
private static int totalOrders = 0; // 전역 상태
private static List<String> processedOrders = new ArrayList<>(); // 전역 상태

// InventoryService.java
private static Map<String, Integer> stock = new HashMap<>(); // 전역 상태
```

---

### 3. 강한 결합 (Tight Coupling)

**위치**: 모든 클래스

**영향**:
- 의존성 주입 불가
- 테스트 어려움
- 유연성 부족

**예시**:
```java
// OrderProcessor.java
this.paymentService = new PaymentService(); // 구체 클래스 직접 생성
this.inventoryService = new InventoryService(); // 구체 클래스 직접 생성
```

---

### 4. 단일 책임 원칙 위반 (SRP Violation)

**위치**: `OrderProcessor.java`

**영향**:
- 거대한 클래스 (God Object)
- 변경 영향 범위 큼
- 테스트 어려움

**책임 목록**:
1. 주문 검증
2. 재고 확인
3. 결제 처리
4. 재고 업데이트
5. 알림 전송
6. 로깅
7. 전역 상태 관리

---

### 5. 방어적 복사 없음 (No Defensive Copying)

**위치**: 모든 클래스의 getter 메서드

**영향**:
- 내부 상태 노출
- 외부에서 직접 수정 가능
- 캡슐화 위반

**예시**:
```java
// OrderProcessor.java
public static List<String> getProcessedOrders() {
    return processedOrders; // 방어적 복사 없음!
}

// InventoryService.java
public static Map<String, Integer> getStock() {
    return stock; // 방어적 복사 없음!
}
```

---

### 6. 테스트 어려움

**원인**:
- 전역 상태 사용
- 의존성 주입 불가
- 하드코딩된 의존성
- 순환 의존성

**영향**:
- 단위 테스트 작성 불가
- 통합 테스트 어려움
- 테스트 격리 불가

---

## 성능 및 메모리 이슈

### 전역 상태 메모리 누수 가능성

```java
// 계속 증가하는 리스트 (메모리 누수 가능)
private static List<String> processedOrders = new ArrayList<>();
private static List<String> notifications = new ArrayList<>();
```

### 동시성 문제

모든 정적 필드가 동기화되지 않아 멀티스레드 환경에서 위험합니다.

---

## 리팩터링 우선순위

### 1단계: 순환 의존성 제거 (최우선)
- **목표**: 순환 의존성 0건
- **방법**: 이벤트 기반 아키텍처 또는 인터페이스 분리

### 2단계: 전역 상태 제거
- **목표**: 모든 정적 필드를 인스턴스 필드로 변경
- **방법**: 의존성 주입, 상태 관리 객체 분리

### 3단계: 인터페이스 분리
- **목표**: 구체 클래스 대신 인터페이스 사용
- **방법**: 인터페이스 추출, 의존성 역전 원칙 적용

### 4단계: 모듈화
- **목표**: 단일 책임 원칙 준수
- **방법**: 클래스 분리, 책임 분리

### 5단계: 테스트 가능성 향상
- **목표**: 테스트 커버리지 80%+
- **방법**: 의존성 주입, 모킹 가능한 구조

---

## 리팩터링 후 예상 구조

```
┌─────────────────┐
│   OrderService  │ (인터페이스 기반)
└────────┬────────┘
         │
         ├─────────────────┬─────────────────┐
         │                 │                 │
         ▼                 ▼                 ▼
┌─────────────────┐ ┌──────────────┐ ┌─────────────────┐
│PaymentProcessor │ │StockManager │ │NotificationSender│
│  (인터페이스)    │ │  (인터페이스) │ │  (인터페이스)    │
└─────────────────┘ └──────────────┘ └─────────────────┘
         │                 │                 │
         ▼                 ▼                 ▼
┌─────────────────┐ ┌──────────────┐ ┌─────────────────┐
│PaymentServiceImpl│ │StockManagerImpl│ │EmailNotifier   │
└─────────────────┘ └──────────────┘ └─────────────────┘
```

### 개선 사항

1. ✅ 순환 의존성 제거
2. ✅ 인터페이스 기반 설계
3. ✅ 의존성 주입 가능
4. ✅ 테스트 가능한 구조
5. ✅ 단일 책임 원칙 준수

---

## 측정 가능한 지표

### 현재 상태

| 지표 | 현재 값 | 목표 |
|------|---------|------|
| 순환 의존성 | 2건 | 0건 |
| 테스트 커버리지 | 0% | 80%+ |
| 전역 상태 | 4개 | 0개 |
| 클래스 크기 (LOC) | ~150 (OrderProcessor) | <50 |
| 의존성 주입 | 없음 | 있음 |

### 리팩터링 후 목표

| 지표 | 목표 값 |
|------|---------|
| 순환 의존성 | 0건 ✅ |
| 테스트 커버리지 | 80%+ ✅ |
| 전역 상태 | 0개 ✅ |
| 최대 클래스 크기 | <50 LOC ✅ |
| 인터페이스 사용 | 100% ✅ |

---

## 참고사항

### 코드 위치

모든 레거시 코드는 `src/main/java/com/example/legacy/` 디렉토리에 위치합니다.

### 실행 방법

```bash
# 상위 디렉토리에서
cd ..
./gradlew.bat run --main-class com.example.legacy.Main
```

### 주의사항

이 레거시 코드는 **의도적으로 문제가 있는 코드**로 작성되었습니다. 
실제 프로덕션 환경에서는 이런 코드를 피해야 합니다.

---

## 문서 버전

- **버전**: 1.0
- **작성일**: 2025-11-05
- **작성 목적**: Task06 리팩터링 작업을 위한 레거시 코드 분석

