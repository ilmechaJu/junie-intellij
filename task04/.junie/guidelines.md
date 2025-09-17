## 시스템 요구사항

### 소프트웨어 요구사항
- JDK 11 이상
- IntelliJ IDEA (Junie 플러그인 설치됨)
- MySQL 8.0 이상 또는 H2 데이터베이스
- Maven 또는 Gradle

### 기술 스택
- **백엔드**: Java, JDBC
- **프론트엔드**: JavaFX 또는 Swing
- **데이터베이스**: MySQL 또는 H2
- **테스트**: JUnit 5
- **빌드 도구**: Maven 또는 Gradle

## 개발 환경 설정

### 1. IntelliJ IDEA 및 Junie 설정

1. IntelliJ IDEA를 실행합니다.
2. Junie 플러그인이 설치되어 있지 않다면 설치합니다:
    - `File` > `Settings`(Windows/Linux) 또는 `IntelliJ IDEA` > `Preferences`(macOS)를 선택합니다.
    - `Plugins`를 클릭하고 `Marketplace` 탭에서 "Junie"를 검색하여 설치합니다.
    - IDE를 재시작합니다.

### 2. 프로젝트 생성

1. `File` > `New` > `Project`를 선택합니다.
2. `Maven` 또는 `Gradle`을 선택합니다.
3. 프로젝트 이름을 "LibraryManagementSystem"으로 지정합니다.
4. GroupId를 "com.example"로, ArtifactId를 "librarysystem"으로 설정합니다.
5. 프로젝트 위치를 선택하고 `Create` 버튼을 클릭합니다.

### 3. 의존성 설정

Junie를 활용하여 필요한 의존성을 추가합니다:

1. `pom.xml`(Maven) 또는 `build.gradle`(Gradle) 파일을 엽니다.
2. Junie를 호출합니다: `Alt+J`(Windows/Linux) 또는 `Option+J`(macOS)
3. 다음 프롬프트를 입력합니다:

```
도서관 관리 시스템을 위한 의존성을 추가해줘. MySQL, JDBC, JUnit 5, JavaFX(또는 Swing), Lombok을 포함해야 해.
```

4. Junie가 제안한 의존성 설정을 검토하고 적용합니다.

### 4. 데이터베이스 설정

#### MySQL 설정

1. MySQL 서버가 설치되어 있고 실행 중인지 확인합니다.
2. MySQL 클라이언트 또는 워크벤치를 사용하여 새 데이터베이스를 생성합니다:

```sql
CREATE DATABASE library_management;
CREATE USER 'library_admin'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON library_management.* TO 'library_admin'@'localhost';
FLUSH PRIVILEGES;
```

#### H2 데이터베이스 설정 (대안)

H2는 가벼운 인메모리 데이터베이스로, 개발 및 테스트 목적으로 사용하기 좋습니다:

1. Maven 또는 Gradle 의존성에 H2를 추가합니다.
2. 애플리케이션 설정에서 H2 데이터베이스 URL을 구성합니다.

## 프로젝트 구조

Junie를 활용하여 MVC 패턴에 따른 프로젝트 구조를 생성합니다:

1. 프로젝트의 루트 디렉토리에서 Junie를 호출합니다.
2. 다음 프롬프트를 입력합니다:

```
MVC 패턴을 사용하는 도서관 관리 시스템의 디렉토리 구조와 필요한 주요 클래스들을 생성해줘. 모델, 뷰, 컨트롤러, 서비스, DAO, 유틸 패키지를 포함해야 해.
```

3. Junie가 제안한 디렉토리 구조와 클래스 목록을 검토하고 적용합니다.

이 프로젝트는 다음과 같은 패키지 구조를 가질 것입니다:

```
com.example.librarysystem/
├── model/             # 데이터 모델 클래스
├── view/              # UI 관련 클래스
├── controller/        # 컨트롤러 클래스
├── service/           # 비즈니스 로직 처리 클래스
├── dao/               # 데이터 액세스 객체
├── util/              # 유틸리티 클래스
├── config/            # 설정 관련 클래스
└── exception/         # 사용자 정의 예외 클래스
```

## 단계별 구현 가이드

### 1. 데이터베이스 스키마 설계

Junie를 활용하여 도서관 관리 시스템에 필요한 데이터베이스 스키마를 설계합니다:

1. 새 SQL 파일(`src/main/resources/schema.sql`)을 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
도서관 관리 시스템을 위한 데이터베이스 스키마를 SQL로 작성해줘. 도서, 회원, 대출, 예약, 카테고리 테이블이 필요해.
```

3. Junie가 생성한 SQL 스키마를 검토하고 필요에 따라 수정합니다.
4. MySQL 클라이언트를 사용하여 스키마를 적용하거나, 애플리케이션에서 자동으로 스키마가 생성되도록 설정합니다.

### 2. 모델 클래스 구현

Junie를 사용하여 데이터베이스 테이블에 대응하는 모델 클래스를 생성합니다:

1. `src/main/java/com/example/librarysystem/model` 디렉토리에서 새 Java 클래스 파일을 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
Book, Member, Loan, Reservation, Category 모델 클래스를 생성해줘. 각 클래스는 해당 테이블의 필드를 포함하고, Lombok 어노테이션을 사용해서 간결하게 작성해줘.
```

3. 각 모델 클래스에 대해 Junie가 제안한 코드를 검토하고 적용합니다.

### 3. DAO(Data Access Object) 클래스 구현

Junie를 사용하여 데이터베이스 접근을 위한 DAO 클래스를 생성합니다:

1. `src/main/java/com/example/librarysystem/dao` 디렉토리에서 인터페이스와 구현 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
JDBC를 사용하여 BookDAO, MemberDAO, LoanDAO, ReservationDAO 인터페이스와 그 구현 클래스를 생성해줘. 각 DAO는 CRUD 작업과 필요한 조회 메소드를 포함해야 해.
```

3. Junie가 제안한 DAO 클래스를 검토하고 적용합니다.

### 4. 서비스 레이어 구현

Junie를 사용하여 비즈니스 로직을 처리할 서비스 클래스를 생성합니다:

1. `src/main/java/com/example/librarysystem/service` 디렉토리에서 인터페이스와 구현 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
BookService, MemberService, LoanService, ReservationService 인터페이스와 구현 클래스를 생성해줘. 각 서비스는 해당 DAO를 사용하고, 비즈니스 로직(예: 대출 가능 여부 확인, 연체료 계산 등)을 포함해야 해.
```

3. Junie가 제안한 서비스 클래스를 검토하고 적용합니다.

### 5. 컨트롤러 구현

Junie를 사용하여 UI와 서비스 레이어를 연결할 컨트롤러 클래스를 생성합니다:

1. `src/main/java/com/example/librarysystem/controller` 디렉토리에서 컨트롤러 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
BookController, MemberController, LoanController, ReservationController 클래스를 생성해줘. 각 컨트롤러는 해당 서비스를 사용하고, UI에서 발생하는 이벤트를 처리하는 메소드를 포함해야 해.
```

3. Junie가 제안한 컨트롤러 클래스를 검토하고 적용합니다.

### 6. UI 구현 (JavaFX 또는 Swing)

Junie를 사용하여 사용자 인터페이스를 구현합니다:

1. `src/main/java/com/example/librarysystem/view` 디렉토리에서 UI 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다(JavaFX 예시):

```
JavaFX를 사용하여 도서관 관리 시스템의 UI를 구현해줘. 메인 화면, 도서 관리 화면, 회원 관리 화면, 대출/반납 화면, 예약 관리 화면이 필요해. FXML을 사용할 수도 있어.
```

또는 Swing을 선호한다면:

```
Swing을 사용하여 도서관 관리 시스템의 UI를 구현해줘. 메인 화면, 도서 관리 화면, 회원 관리 화면, 대출/반납 화면, 예약 관리 화면이 필요해.
```

3. Junie가 제안한 UI 코드를 검토하고 적용합니다.

### 7. 메인 애플리케이션 클래스 구현

Junie를 사용하여 애플리케이션 시작점을 구현합니다:

1. `src/main/java/com/example/librarysystem` 디렉토리에서 메인 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
도서관 관리 시스템의 메인 애플리케이션 클래스를 생성해줘. 필요한 초기화 작업을 수행하고 UI를 시작해야 해.
```

3. Junie가 제안한 메인 클래스 코드를 검토하고 적용합니다.

## 테스트 및 배포

### 1. JUnit 테스트 작성

Junie를 사용하여 DAO와 서비스 클래스에 대한 단위 테스트를 작성합니다:

1. `src/test/java/com/example/librarysystem` 디렉토리에 테스트 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
BookDAO, MemberDAO, BookService, LoanService에 대한 JUnit 5 테스트 클래스를 작성해줘. 모킹을 사용하여 외부 의존성을 처리하고, 주요 기능을 테스트하는 메소드를 포함해야 해.
```

3. Junie가 제안한 테스트 코드를 검토하고 적용합니다.

### 2. 통합 테스트 작성

Junie를 사용하여 실제 데이터베이스를 사용하는 통합 테스트를 작성합니다:

1. `src/test/java/com/example/librarysystem/integration` 디렉토리에 통합 테스트 클래스를 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
H2 인메모리 데이터베이스를 사용하여 BookDAO, LoanDAO에 대한 통합 테스트를 작성해줘. 테스트 실행 전에 필요한 스키마와 데이터를 초기화하는 코드를 포함해야 해.
```

3. Junie가 제안한 통합 테스트 코드를 검토하고 적용합니다.

### 3. 빌드 및 패키징

Junie를 사용하여 애플리케이션 빌드 및 패키징 방법을 구성합니다:

1. Maven 프로젝트의 경우 `pom.xml` 파일을 수정하고, Gradle 프로젝트의 경우 `build.gradle` 파일을 수정합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
Maven(또는 Gradle)을 사용하여 도서관 관리 시스템을 실행 가능한 JAR 파일로 패키징하는 설정을 추가해줘. 모든 의존성이 포함되어야 해.
```

3. Junie가 제안한 빌드 설정을 검토하고 적용합니다.

### 4. 실행 및 배포 가이드 작성

Junie를 사용하여 애플리케이션 실행 및 배포 가이드를 작성합니다:

1. 프로젝트 루트 디렉토리에 `README.md` 파일을 생성합니다.
2. Junie를 호출하고 다음 프롬프트를 입력합니다:

```
도서관 관리 시스템의 설치, 설정, 빌드, 실행 방법을 설명하는 README.md 파일을 작성해줘. 개발 환경 설정, 데이터베이스 설정, 빌드 및 실행 명령어를 포함해야 해.
```

3. Junie가 제안한 README 내용을 검토하고 적용합니다.

## 추가 기능 및 확장

프로젝트의 기본 기능이 구현된 후, Junie를 활용하여 다음과 같은 추가 기능을 구현할 수 있습니다:

### 1. 권한 관리 및 로그인 시스템

```
도서관 관리 시스템에 로그인 기능과 사용자 권한 관리(관리자, 사서, 일반 회원) 기능을 추가해줘.
```

### 2. 대시보드 및 통계

```
도서관 관리 시스템에 대시보드 기능을 추가해줘. 인기 도서, 활발한 회원, 월별 대출 통계 등을 시각적으로 표시해야 해.
```

### 3. 이메일 알림 시스템

```
도서관 관리 시스템에 이메일 알림 기능을 추가해줘. 대출 만기 알림, 예약 도서 도착 알림, 연체 알림 등을 보낼 수 있어야 해.
```

### 4. 바코드 스캔 통합

```
도서관 관리 시스템에 바코드 스캔 기능을 추가해줘. 웹캠이나 바코드 스캐너를 통해 도서 정보를 빠르게 입력할 수 있어야 해.
```

### 5. 외부 API 연동

```
도서관 관리 시스템에 Google Books API나 Open Library API를 연동해서 도서 정보를 자동으로 가져오는 기능을 추가해줘.
```

---

