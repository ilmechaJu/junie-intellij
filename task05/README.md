# Junie와 MCP 연동 가이드

이 가이드는 Junie 설정에서 MCP(Model Checkpoint Protocol)를 연동하는 방법을 설명합니다. 프로젝트 구조를 트리 형태로 안내하고, 필요한 mcp.json 파일을 생성하여 GitHub MCP를 구현하는 방법을 자세히 알려드립니다.

## 목차
1. [MCP 개요](#mcp-개요)
2. [사전 준비](#사전-준비)
3. [프로젝트 구조 설정](#프로젝트-구조-설정)
4. [.junie 디렉토리 설정](#junie-디렉토리-설정)
5. [mcp.json 파일 구성](#mcpjson-파일-구성)
6. [GitHub MCP 연동](#github-mcp-연동)
7. [연동 테스트 및 확인](#연동-테스트-및-확인)
8. [문제 해결](#문제-해결)

## MCP 개요

MCP(Model Checkpoint Protocol)는 AI 모델 체크포인트를 관리하고 버전 제어하기 위한 프로토콜입니다. Junie와 MCP를 연동하면 다음과 같은 이점이 있습니다:

- 모델 체크포인트의 일관된 관리
- 프로젝트 간 모델 공유 및 재사용 용이
- 버전 관리를 통한 안정적인 AI 개발 환경 구축
- GitHub와 연동하여 협업 작업 지원

## 사전 준비

MCP 연동을 위해 다음 사항을 확인하세요:

1. IntelliJ IDEA가 설치되어 있어야 합니다.
2. Junie 플러그인이 설치 및 활성화되어 있어야 합니다.
3. Git이 설치되어 있어야 합니다.
4. GitHub 계정이 필요합니다.
5. 기본적인 JSON 형식에 대한 이해가 있어야 합니다.

## 프로젝트 구조 설정

MCP를 연동하기 위한 프로젝트 구조는 다음과 같습니다:

```
my-project/                    # 프로젝트 루트 디렉토리
├── .junie/                    # Junie 설정 디렉토리
│   ├── mcp.json              # MCP 구성 파일
│   └── models/               # 로컬 모델 저장 디렉토리
├── src/                       # 소스 코드 디렉토리
│   ├── main/
│   │   ├── java/             # Java 소스 코드
│   │   └── resources/        # 리소스 파일
│   └── test/                  # 테스트 코드
├── .gitignore                 # Git 무시 파일 목록
├── pom.xml 또는 build.gradle  # 빌드 설정 파일
└── README.md                  # 프로젝트 설명 파일
```

### 프로젝트 구조 생성 방법

1. IntelliJ IDEA에서 새 프로젝트를 생성합니다.
2. 프로젝트가 생성되면, 프로젝트 루트 디렉토리에 `.junie` 디렉토리를 생성합니다:
   - 프로젝트 창에서 프로젝트 루트를 마우스 오른쪽 버튼으로 클릭합니다.
   - `New` > `Directory`를 선택합니다.
   - `.junie`라는 이름을 입력하고 `OK`를 클릭합니다.

3. `.junie` 디렉토리 내에 `models` 디렉토리를 생성합니다:
   - `.junie` 디렉토리를 마우스 오른쪽 버튼으로 클릭합니다.
   - `New` > `Directory`를 선택합니다.
   - `models`라는 이름을 입력하고 `OK`를 클릭합니다.

## .junie 디렉토리 설정

`.junie` 디렉토리는 Junie 플러그인의 설정 파일과 관련 리소스를 저장하는 디렉토리입니다. MCP 연동을 위해서는 이 디렉토리 내에 `mcp.json` 파일을 생성해야 합니다.

### .junie 디렉토리 생성이 안 될 경우

일부 운영 체제에서는 점(.)으로 시작하는 디렉토리 생성이 어려울 수 있습니다. 이 경우 다음 방법을 사용할 수 있습니다:

#### Windows에서:
1. 명령 프롬프트(CMD)를 관리자 권한으로 실행합니다.
2. 프로젝트 디렉토리로 이동합니다:
   ```
   cd path\to\your\project
   ```
3. .junie 디렉토리를 생성합니다:
   ```
   mkdir .junie
   ```

#### macOS/Linux에서:
1. 터미널을 엽니다.
2. 프로젝트 디렉토리로 이동합니다:
   ```
   cd path/to/your/project
   ```
3. .junie 디렉토리를 생성합니다:
   ```
   mkdir .junie
   ```

## mcp.json 파일 구성

MCP 연동을 위해 `.junie` 디렉토리 내에 `mcp.json` 파일을 생성해야 합니다:

1. `.junie` 디렉토리를 마우스 오른쪽 버튼으로 클릭합니다.
2. `New` > `File`을 선택합니다.
3. `mcp.json`이라는 이름을 입력하고 `OK`를 클릭합니다.

### 기본 mcp.json 구성

`mcp.json` 파일에는 다음과 같은 기본 구성이 포함되어야 합니다:

```json
{
  "version": "1.0",
  "provider": "github",
  "settings": {
    "repository": "username/repo-name",
    "branch": "main",
    "path": "models",
    "auth": {
      "type": "token",
      "token": "${env:GITHUB_TOKEN}"
    }
  },
  "models": [
    {
      "name": "my-model",
      "version": "1.0.0",
      "description": "My trained model",
      "path": "my-model/latest"
    }
  ],
  "cache": {
    "location": "${project}/.junie/models",
    "ttl": 86400
  }
}
```

### mcp.json 필드 설명

- **version**: MCP 구성 파일의 버전
- **provider**: 모델 저장소 제공자 (github, local, s3 등)
- **settings**: 제공자별 설정
  - **repository**: GitHub 저장소 이름 (username/repo-name 형식)
  - **branch**: 사용할 Git 브랜치
  - **path**: 저장소 내 모델 디렉토리 경로
  - **auth**: 인증 정보
    - **type**: 인증 유형 (token, ssh, basic)
    - **token**: GitHub 개인 액세스 토큰 (환경 변수에서 가져오는 것이 안전)
- **models**: 사용할 모델 목록
  - **name**: 모델 이름
  - **version**: 모델 버전
  - **description**: 모델 설명
  - **path**: 모델 파일 경로
- **cache**: 로컬 캐시 설정
  - **location**: 캐시 위치 (상대 경로 또는 절대 경로)
  - **ttl**: 캐시 유효 시간(초)

## GitHub MCP 연동

GitHub와 MCP를 연동하기 위한 단계별 가이드입니다:

### 1. GitHub 저장소 생성

1. GitHub에 로그인합니다.
2. 새 저장소를 생성합니다:
   - 오른쪽 상단의 + 아이콘을 클릭하고 "New repository"를 선택합니다.
   - 저장소 이름을 입력합니다 (예: "junie-mcp-models").
   - 필요에 따라 설명을 추가합니다.
   - 저장소를 Public 또는 Private으로 설정합니다.
   - "Create repository" 버튼을 클릭합니다.

### 2. GitHub 개인 액세스 토큰 생성

1. GitHub 계정 설정으로 이동합니다:
   - 오른쪽 상단의 프로필 아이콘을 클릭합니다.
   - "Settings"를 선택합니다.
2. 왼쪽 사이드바에서 "Developer settings"를 클릭합니다.
3. "Personal access tokens"를 클릭하고 "Tokens (classic)"을 선택합니다.
4. "Generate new token"을 클릭하고 필요한 경우 "Generate new token (classic)"을 선택합니다.
5. 토큰 이름을 입력합니다 (예: "Junie MCP Token").
6. 다음 권한을 선택합니다:
   - `repo` (모든 저장소 권한)
   - `read:packages` (패키지 읽기 권한)
   - `write:packages` (패키지 쓰기 권한)
7. "Generate token" 버튼을 클릭합니다.
8. 생성된 토큰을 안전한 곳에 저장합니다 (이 페이지를 벗어나면 다시 볼 수 없습니다).

### 3. 환경 변수 설정

GitHub 토큰을 환경 변수로 설정하여 보안을 유지합니다:

#### Windows에서:
1. 시스템 속성 > 고급 > 환경 변수를 엽니다.
2. 사용자 변수 섹션에서 "새로 만들기"를 클릭합니다.
3. 변수 이름에 `GITHUB_TOKEN`을 입력합니다.
4. 변수 값에 생성한 GitHub 토큰을 입력합니다.
5. "확인"을 클릭하여 저장합니다.

#### macOS/Linux에서:
1. ~/.bash_profile 또는 ~/.zshrc 파일을 편집합니다:
   ```
   nano ~/.bash_profile
   ```
   또는
   ```
   nano ~/.zshrc
   ```
2. 다음 줄을 추가합니다:
   ```
   export GITHUB_TOKEN="your-token-here"
   ```
3. 파일을 저장하고 터미널을 다시 시작하거나 다음 명령어를 실행합니다:
   ```
   source ~/.bash_profile
   ```
   또는
   ```
   source ~/.zshrc
   ```

### 4. mcp.json 파일 업데이트

생성한 GitHub 저장소와 연동하도록 mcp.json 파일을 업데이트합니다:

```json
{
  "version": "1.0",
  "provider": "github",
  "settings": {
    "repository": "your-username/junie-mcp-models",
    "branch": "main",
    "path": "models",
    "auth": {
      "type": "token",
      "token": "${env:GITHUB_TOKEN}"
    }
  },
  "models": [
    {
      "name": "example-model",
      "version": "1.0.0",
      "description": "Example model for Junie MCP",
      "path": "example-model/latest"
    }
  ],
  "cache": {
    "location": "${project}/.junie/models",
    "ttl": 86400
  }
}
```

`your-username`을 실제 GitHub 사용자 이름으로 바꿉니다.

### 5. 모델 디렉토리 구조 생성

GitHub 저장소에 다음과 같은 구조를 생성합니다:

```
junie-mcp-models/
└── models/
    └── example-model/
        └── latest/
            ├── model.bin
            └── metadata.json
```

1. 저장소를 로컬에 클론합니다:
   ```
   git clone https://github.com/your-username/junie-mcp-models.git
   ```

2. 필요한 디렉토리 구조를 생성합니다:
   ```
   cd junie-mcp-models
   mkdir -p models/example-model/latest
   ```

3. 간단한 metadata.json 파일을 생성합니다:
   ```json
   {
     "name": "example-model",
     "version": "1.0.0",
     "created": "2025-09-12T00:00:00Z",
     "framework": "junie",
     "description": "Example model for Junie MCP",
     "author": "Your Name",
     "license": "MIT"
   }
   ```

4. 변경 사항을 커밋하고 푸시합니다:
   ```
   git add .
   git commit -m "Initial model structure"
   git push origin main
   ```

## 연동 테스트 및 확인

Junie와 MCP 연동이 제대로 작동하는지 확인합니다:

### 1. IntelliJ IDEA에서 Junie 설정 확인

1. IntelliJ IDEA에서 `Tools` > `Junie` > `Settings`를 선택합니다.
2. "Model" 또는 "MCP" 탭을 선택합니다.
3. "Refresh Models" 또는 "Sync MCP" 버튼을 클릭합니다.
4. 설정된 모델이 목록에 표시되는지 확인합니다.

### 2. Junie 명령을 통한 확인

1. 편집기에서 Junie를 호출합니다: `Alt+J`(Windows/Linux) 또는 `Option+J`(macOS)
2. 다음 프롬프트를 입력합니다:
   ```
   MCP 설정을 확인하고 연결된 모델을 보여줘
   ```
3. Junie가 연결된 MCP 설정과 사용 가능한 모델을 보여주는지 확인합니다.

## 문제 해결

MCP 연동 과정에서 발생할 수 있는 일반적인 문제와 해결 방법입니다:

### 인증 오류

- **문제**: GitHub 토큰 인증 오류가 발생하는 경우
- **해결방법**:
  1. 환경 변수 `GITHUB_TOKEN`이 올바르게 설정되었는지 확인합니다.
  2. GitHub 토큰이 필요한 권한을 가지고 있는지 확인합니다.
  3. 토큰이 만료되지 않았는지 확인하고, 필요한 경우 새 토큰을 생성합니다.

### 저장소 경로 오류

- **문제**: 모델 경로를 찾을 수 없는 경우
- **해결방법**:
  1. mcp.json의 `repository` 값이 올바른지 확인합니다.
  2. `path` 설정이 GitHub 저장소의 실제 경로와 일치하는지 확인합니다.
  3. `branch` 설정이 올바른지 확인합니다.

### 캐시 문제

- **문제**: 모델이 로컬 캐시에 저장되지 않는 경우
- **해결방법**:
  1. `.junie/models` 디렉토리가 존재하고 쓰기 가능한지 확인합니다.
  2. mcp.json의 `cache.location` 경로가 올바른지 확인합니다.
  3. Junie 설정에서 캐시를 수동으로 비우고 다시 동기화합니다.

### 로그 확인

문제 진단을 위해 Junie 로그를 확인합니다:

1. `Help` > `Show Log in Explorer`(Windows), `Show Log in Finder`(macOS), 또는 `Show Log in Files`(Linux)를 선택합니다.
2. 로그 파일에서 "MCP" 또는 "Model Checkpoint Protocol"을 검색하여 관련 오류 메시지를 찾습니다.

---

이 가이드를 따라 Junie와 MCP를 성공적으로 연동하면, 프로젝트에서 일관된 AI 모델을 사용하고 팀원들과 효율적으로 공유할 수 있습니다. GitHub를 통한 MCP 연동은 모델 버전 관리와 협업을 크게 향상시키는 강력한 기능입니다.