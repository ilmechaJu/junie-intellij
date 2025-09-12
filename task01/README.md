# IntelliJ IDEA에서 Junie 플러그인 사용 가이드

이 가이드는 IntelliJ IDEA에서 Junie 플러그인을 설치하고, 활성화하며, 사용하는 방법에 대한 상세한 안내입니다.

## 목차
1. [Junie 플러그인 소개](#junie-플러그인-소개)
2. [설치 방법](#설치-방법)
3. [활성화 방법](#활성화-방법)
4. [기본 사용법](#기본-사용법)
5. [고급 기능](#고급-기능)
6. [문제 해결](#문제-해결)

## Junie 플러그인 소개

Junie는 IntelliJ IDEA를 위한 AI 코딩 어시스턴트 플러그인으로, 코드 작성, 리팩토링, 디버깅 등 다양한 개발 작업을 지원합니다. JetBrains에서 개발한 이 플러그인은 개발자의 생산성을 크게 향상시키는 데 도움이 됩니다.

## 설치 방법

### 방법 1: IntelliJ IDEA 내부 플러그인 마켓플레이스에서 설치

1. IntelliJ IDEA를 실행합니다.
2. `File` > `Settings`(Windows/Linux) 또는 `IntelliJ IDEA` > `Preferences`(macOS)를 선택합니다.
3. 왼쪽 패널에서 `Plugins`를 클릭합니다.
4. `Marketplace` 탭을 선택합니다.
5. 검색 상자에 `Junie`를 입력합니다.
6. 검색 결과에서 Junie 플러그인을 찾아 `Install` 버튼을 클릭합니다.
7. 설치가 완료되면 IDE를 재시작합니다.

### 방법 2: 플러그인 웹사이트에서 다운로드하여 설치

1. [JetBrains 플러그인 저장소](https://plugins.jetbrains.com/)에 접속합니다.
2. 검색 상자에 `Junie`를 입력하여 플러그인을 찾습니다.
3. Junie 플러그인 페이지에서 사용 중인 IntelliJ IDEA 버전과 호환되는 버전을 다운로드합니다.
4. IntelliJ IDEA를 실행하고 `File` > `Settings`(Windows/Linux) 또는 `IntelliJ IDEA` > `Preferences`(macOS)를 선택합니다.
5. 왼쪽 패널에서 `Plugins`를 클릭하고 설정 아이콘(⚙️)을 클릭한 후 `Install Plugin from Disk...`를 선택합니다.
6. 다운로드한 플러그인 파일을 선택하고 `OK`를 클릭합니다.
7. IDE를 재시작하여 플러그인을 활성화합니다.

## 활성화 방법

Junie 플러그인을 설치한 후 활성화하려면 다음 단계를 따르세요:

1. IntelliJ IDEA를 재시작한 후, `Tools` 메뉴에서 `Junie` 옵션을 찾을 수 있습니다.
2. `Junie` > `Settings`를 클릭하여 설정 창을 엽니다.
3. API 키가 필요한 경우, 키를 입력하고 `Apply` 또는 `OK`를 클릭합니다.
   - API 키는 [JetBrains 계정](https://account.jetbrains.com/)에서 확인할 수 있습니다.
   - 또는 무료 체험판을 사용할 수 있는 옵션을 선택할 수 있습니다.
4. 활성화 상태를 확인하려면 IDE 하단 상태 표시줄에서 Junie 아이콘을 확인하세요. 녹색으로 표시되면 활성화된 상태입니다.

## 기본 사용법

Junie를 사용하는 기본적인 방법은 다음과 같습니다:

### 코드 생성 및 완성

1. 코드 편집기에서 작업 중 코드 생성이 필요한 위치에 커서를 놓습니다.
2. `Alt+J`(Windows/Linux) 또는 `Option+J`(macOS) 단축키를 누르거나, 마우스 오른쪽 버튼을 클릭하고 컨텍스트 메뉴에서 `Junie` > `Generate Code`를 선택합니다.
3. 원하는 코드 기능에 대한 설명을 자연어로 입력합니다. 예: "사용자 로그인 검증 메서드 작성"
4. Junie가 코드를 생성하면 이를 검토하고 필요한 경우 수정합니다.

### 코드 리팩토링

1. 리팩토링할 코드 블록을 선택합니다.
2. `Alt+Shift+J`(Windows/Linux) 또는 `Option+Shift+J`(macOS) 단축키를 누르거나, 마우스 오른쪽 버튼을 클릭하고 `Junie` > `Refactor Code`를 선택합니다.
3. 원하는 리팩토링 유형을 선택하거나 자연어 설명을 입력합니다.
4. 제안된 리팩토링을 검토하고 적용합니다.

### 코드 설명 및 이해

1. 이해하기 어려운 코드 블록을 선택합니다.
2. `Ctrl+Alt+J`(Windows/Linux) 또는 `Cmd+Option+J`(macOS) 단축키를 누르거나, 마우스 오른쪽 버튼을 클릭하고 `Junie` > `Explain Code`를 선택합니다.
3. Junie가 선택한 코드에 대한 자세한 설명을 제공합니다.

## 고급 기능

Junie는 다음과 같은 고급 기능을 제공합니다:

### 단위 테스트 생성

1. 테스트가 필요한 클래스나 메서드에 커서를 놓습니다.
2. `Junie` > `Generate Tests`를 선택합니다.
3. 원하는 테스트 프레임워크(JUnit, TestNG 등)와 테스트 범위를 선택합니다.
4. 생성된 테스트 코드를 검토하고 필요에 따라 수정합니다.

### 코드 리뷰 및 최적화

1. 리뷰가 필요한 파일이나 코드 블록을 선택합니다.
2. `Junie` > `Review Code`를 선택합니다.
3. Junie가 코드 품질, 성능, 보안 등의 측면에서 개선 제안을 제공합니다.

### 프로젝트 분석

1. `Junie` > `Analyze Project`를 선택합니다.
2. 프로젝트 구조, 의존성, 아키텍처 패턴 등에 대한 종합적인 분석을 받습니다.

## 문제 해결

Junie 플러그인 사용 중 문제가 발생할 경우 다음 조치를 취하세요:

### 일반적인 문제

1. **플러그인이 응답하지 않음**
   - IntelliJ IDEA를 재시작합니다.
   - `File` > `Invalidate Caches / Restart...`를 선택하고 캐시를 초기화합니다.

2. **API 키 오류**
   - 설정에서 API 키가 올바르게 입력되었는지 확인합니다.
   - JetBrains 계정에서 구독 상태를 확인합니다.

3. **성능 문제**
   - `Junie` > `Settings`에서 성능 옵션을 조정합니다.
   - 불필요한 플러그인을 비활성화하여 IDE 성능을 개선합니다.

### 로그 확인 방법

문제를 더 자세히 진단하려면 로그 파일을 확인하세요:

1. `Help` > `Show Log in Explorer`(Windows), `Show Log in Finder`(macOS), 또는 `Show Log in Files`(Linux)를 선택합니다.
2. 로그 파일에서 "Junie"를 검색하여 관련 오류 메시지를 찾습니다.

### 지원 요청

추가 지원이 필요한 경우:
1. [JetBrains 지원 포털](https://www.jetbrains.com/support/)을 방문하세요.
2. `Junie` > `Help` > `Report Issue`를 통해 직접 문제를 보고할 수 있습니다.

---

Junie 플러그인을 통해 개발 생산성을 크게 향상시키고, AI의 도움으로 더 나은 코드를 작성하세요!