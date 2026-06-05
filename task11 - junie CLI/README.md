# Junie CLI 활용 가이드 — 터미널에서 AI 코딩 에이전트 사용하기

이 가이드는 JetBrains Junie의 CLI(Command Line Interface) 기능을 활용하여 터미널 환경에서 AI 코딩 에이전트를 사용하는 방법을 학습합니다. IntelliJ IDEA 플러그인(Task 01~05)과는 별개로, **터미널 기반**의 독립적인 Junie 사용법을 다룹니다.

## 목차
1. [Junie CLI 소개](#junie-cli-소개)
2. [설치 방법](#설치-방법)
3. [인증 및 초기 설정](#인증-및-초기-설정)
4. [인터랙티브 모드 사용법](#인터랙티브-모드-사용법)
5. [작업 모드 이해하기](#작업-모드-이해하기)
6. [슬래시 커맨드](#슬래시-커맨드)
7. [BYOK — 나만의 LLM 연결하기](#byok--나만의-llm-연결하기)
8. [Headless 모드와 CI/CD 연동](#headless-모드와-cicd-연동)
9. [로컬 코드 리뷰](#로컬-코드-리뷰)
10. [확장 기능](#확장-기능)
11. [실전 프롬프트 예제](#실전-프롬프트-예제)
12. [문제 해결](#문제-해결)

---

## Junie CLI 소개

Junie CLI는 JetBrains에서 제공하는 **터미널 기반 AI 코딩 에이전트**입니다. IDE 없이도 터미널에서 직접 코드 작성, 리팩터링, 테스트, 디버깅, 코드 리뷰 등 다양한 개발 작업을 수행할 수 있습니다.

### IntelliJ 플러그인과의 차이점

| 구분 | IntelliJ 플러그인 (Task 01~05) | Junie CLI (본 가이드) |
|------|-------------------------------|----------------------|
| 실행 환경 | IntelliJ IDEA 내부 | 모든 터미널/쉘 |
| OS 지원 | IDE가 지원하는 환경 | Windows, macOS, Linux |
| CI/CD 연동 | 불가 | Headless 모드 지원 |
| LLM 선택 | IDE 설정 기반 | BYOK로 자유 선택 |
| 디버깅 | IDE 디버거 활용 | Debug 모드 (IDE 연동) |
| 코드 리뷰 | PR 리뷰 | 로컬 변경사항 리뷰 |

### 주요 장점
- **IDE 독립적**: IntelliJ 없이도 완전한 기능 사용 가능
- **CI/CD 통합**: 자동화된 코드 리뷰, 품질 개선, 테스트 수행
- **LLM 자유 선택**: OpenAI, Anthropic, Google, xAI 등 원하는 모델 사용
- **경량**: 터미널만 있으면 어디서든 실행 가능

---

## 설치 방법

### Windows (PowerShell)

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -Command "iex (irm 'https://junie.jetbrains.com/install.ps1')"
```

### macOS/Linux (Shell)

```bash
curl -fsSL https://junie.jetbrains.com/install.sh | bash
```

### macOS (Homebrew)

```bash
brew tap jetbrains/junie
brew update
brew install junie
```

### 설치 확인

```bash
junie --version
```

> **참고**: 설치 후 터미널을 재시작하거나 PATH를 다시 로드해야 할 수 있습니다.

---

## 인증 및 초기 설정

### Step 1: Junie 실행

프로젝트 루트 디렉토리에서 `junie` 명령어를 실행합니다:

```bash
cd /path/to/your/project
junie
```

### Step 2: 인증 방법 선택

Junie CLI는 세 가지 인증 방법을 제공합니다:

#### 방법 1: JetBrains Account 로그인
- AI Pro 또는 Ultimate 구독 필요
- 브라우저 리디렉션을 통한 로그인

#### 방법 2: Junie API Key
- 사용량 기반 과금
- [junie.jetbrains.com/cli](https://junie.jetbrains.com/cli)에서 토큰 생성
- 환경 변수 또는 플래그로 전달:
  ```bash
  junie --auth perm-xxxxxxxxxxxx
  ```

#### 방법 3: BYOK (Bring Your Own Key)
- 별도의 JetBrains 구독 없이 자체 LLM API 키 사용
- 상세 설정은 [BYOK 섹션](#byok--나만의-llm-연결하기) 참조

### 설정 파일 구조

Junie CLI는 다음 위치에서 설정을 읽어옵니다:

```
~/.junie/                      # 사용자 전역 설정
├── config.json               # 전역 설정 파일
├── allowlist.json            # 명령 자동 승인 목록
└── ...

<project>/.junie/              # 프로젝트별 설정
├── config.json               # 프로젝트 설정
├── guidelines.md             # 프로젝트 가이드라인
├── mcp.json                  # MCP 서버 설정
└── commands/                 # 커스텀 슬래시 커맨드
```

---

## 인터랙티브 모드 사용법

### 기본 사용

```bash
junie
```

실행 후 프롬프트에 자연어로 작업을 지시합니다:

```
> 이 프로젝트의 구조를 설명해줘
> LoginController의 인증 로직을 리팩터링해줘
> UserService에 대한 단위 테스트를 작성해줘
```

### 파일/폴더 참조 (`@`)

프롬프트에서 `@`를 사용하여 특정 파일이나 폴더를 컨텍스트에 포함시킬 수 있습니다:

```
> @src/main/java/UserService.java 이 파일의 메서드 구조를 분석해줘
> @src/test/ 이 디렉토리의 테스트 커버리지를 개선해줘
```

> **팁**: 파일이나 이미지를 터미널 창에 드래그 앤 드롭으로 참조할 수도 있습니다.

### 실시간 후속 지시

Junie가 작업 중일 때 기다리지 않고 추가 지시를 입력할 수 있습니다. 추가된 내용은 즉시 반영됩니다:

```
> REST API 엔드포인트를 만들어줘
  (Junie 작업 중...)
> 아, 그리고 입력 검증도 추가해줘
```

### 쉘 명령어 실행

Junie CLI 내에서 `!` 접두사로 쉘 명령어를 직접 실행할 수 있습니다:

```
!ls -la
!git status
!./gradlew test
```

### 프롬프트 히스토리 검색

`Ctrl+R`로 이전 세션의 프롬프트를 검색할 수 있습니다. 히스토리는 세션 간에 유지됩니다.

---

## 작업 모드 이해하기

Junie CLI는 상황에 맞는 다양한 작업 모드를 제공합니다:

### Code 모드 (기본)
- **전체 에이전트 기능**: 코드 편집, 파일 생성, 터미널 명령 실행, 테스트 수행
- **사용자 승인**: 민감한 작업(셸 명령, 프로젝트 외부 파일 편집 등) 전 승인 요청
- **활용 예시**:
  ```
  > 로그인 모듈의 버그를 수정해줘
  > 새로운 REST 엔드포인트를 추가해줘
  ```

### Plan 모드
- **읽기 전용 분석** 후 설계 문서를 먼저 생성
- 코드 변경 없이 계획만 수립
- **전환 방법**: `Shift+Tab` 또는 `/plan`
- **활용 예시**:
  ```
  /plan 이 레거시 모듈을 마이크로서비스로 분리하는 계획을 세워줘
  ```

### Debug 모드
- 연결된 JetBrains IDE의 디버거와 연동
- 브레이크포인트 관리, 런타임 상태 검사, 표현식 평가
- **전환 방법**: `Shift+Tab+Tab` 또는 `/debug`
- **활용 예시**:
  ```
  /debug NullPointerException이 발생하는 원인을 찾아줘
  ```

### Brave 모드
- 모든 민감한 작업을 **자동 승인** (사용자 확인 없음)
- CI/CD 파이프라인이나 안전한 환경에서 유용
- **전환 방법**: `Ctrl+B` 또는 `/brave`

> ⚠️ **주의**: Brave 모드는 클린 환경(새 브랜치, 컨테이너 등)에서만 사용을 권장합니다.

### 모드 간 전환 요약

| 모드 | 단축키 | 슬래시 커맨드 | 파일 편집 | 명령 실행 |
|------|--------|-------------|----------|----------|
| Code | (기본) | — | ✅ (승인 후) | ✅ (승인 후) |
| Plan | `Shift+Tab` | `/plan` | ❌ | ❌ (읽기만) |
| Debug | `Shift+Tab+Tab` | `/debug` | ❌ | ✅ (디버거) |
| Brave | `Ctrl+B` | `/brave` | ✅ (자동) | ✅ (자동) |

---

## 슬래시 커맨드

프롬프트에서 `/`를 입력하면 사용 가능한 슬래시 커맨드 목록을 볼 수 있습니다.

### 주요 내장 슬래시 커맨드

| 커맨드 | 설명 | 예시 |
|--------|------|------|
| `/new` | 새 세션 시작 (컨텍스트 초기화) | `/new 테스트 작성해줘` |
| `/plan` | Plan 모드 전환 | `/plan 리팩터링 전략 수립` |
| `/debug` | Debug 모드 전환 | `/debug 로그인 오류 분석` |
| `/brave` | Brave 모드 토글 | `/brave` |
| `/model` | LLM 모델 및 Effort 레벨 변경 | `/model` |
| `/effort` | 추론 노력 레벨만 변경 | `/effort` |
| `/review` | 로컬 코드 리뷰 시작 | `/review` |
| `/history` | 이전 세션 목록 및 재개 | `/history` |
| `/account` | 인증/API 키 관리 | `/account` |
| `/usage` | 현재 세션 토큰 사용량/비용 확인 | `/usage` |
| `/remote` | 웹 앱으로 세션 공유 | `/remote` |
| `/quit` | Junie CLI 종료 | `/quit` |

### 커스텀 슬래시 커맨드

반복적인 작업을 위해 커스텀 슬래시 커맨드를 생성할 수 있습니다:

**위치**: `<project>/.junie/commands/` 또는 `~/.junie/commands/`

예시 — `/fix-tests` 커맨드 생성:

```
# .junie/commands/fix-tests.md

실패하는 테스트를 분석하고 수정해줘.
1. 먼저 `./gradlew test`를 실행해서 실패 목록을 확인
2. 각 실패 원인을 분석
3. 코드 또는 테스트를 수정
4. 다시 테스트를 실행하여 통과 확인
```

사용법:
```
/fix-tests
```

---

## BYOK — 나만의 LLM 연결하기

JetBrains 구독 없이도 자체 API 키로 Junie CLI를 사용할 수 있습니다.

### 지원 프로바이더

| 프로바이더 | 플래그 | 모델 예시 |
|-----------|--------|----------|
| Anthropic | `--anthropic-api-key` | Claude Sonnet, Opus |
| OpenAI | `--openai-api-key` | GPT-4, GPT-5 |
| Google | `--google-api-key` | Gemini |
| xAI | `--grok-api-key` | Grok |
| OpenRouter | `--openrouter-api-key` | 다양한 LLM (통합 API) |

### 사용 예시

```bash
# Anthropic Claude 사용
junie --anthropic-api-key sk-ant-xxxx --model sonnet

# OpenAI GPT 사용
junie --openai-api-key sk-xxxx --model gpt-4

# OpenRouter를 통한 다양한 모델 사용
junie --openrouter-api-key sk-or-xxxx --model "anthropic/claude-sonnet"
```

### 환경 변수로 설정 (권장)

매번 플래그를 입력하지 않고 환경 변수로 설정할 수 있습니다:

**Windows (PowerShell)**:
```powershell
$env:ANTHROPIC_API_KEY = "sk-ant-xxxx"
junie --model sonnet
```

**macOS/Linux**:
```bash
export ANTHROPIC_API_KEY="sk-ant-xxxx"
junie --model sonnet
```

### 모델 및 Effort 레벨 선택

인터랙티브 모드에서 `/model` 커맨드로 모델과 추론 강도를 선택할 수 있습니다:

- **Effort 레벨**: `Low`, `Medium`, `High`, `XHigh`, `Max`
- **기본 Effort 설정**: `--effort <level>` 또는 `JUNIE_EFFORT` 환경 변수

```bash
junie --model sonnet --effort high "복잡한 알고리즘 최적화"
```

---

## Headless 모드와 CI/CD 연동

Junie CLI를 CI/CD 파이프라인에서 비대화형(headless)으로 실행할 수 있습니다.

### 기본 사용법

```bash
junie --auth="$JUNIE_API_KEY" "최근 커밋의 코드 품질 이슈를 리뷰하고 수정해줘"
```

### GitHub Actions 연동 예시

```yaml
name: Junie Code Review
on: [pull_request]

jobs:
  junie-review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Install Junie CLI
        run: curl -fsSL https://junie.jetbrains.com/install.sh | bash

      - name: Run Junie Review
        env:
          JUNIE_API_KEY: ${{ secrets.JUNIE_API_KEY }}
        run: |
          junie --auth="$JUNIE_API_KEY" --review "PR 변경사항을 리뷰하고 품질 이슈를 보고해줘"
```

### GitLab CI/CD 연동 예시

```yaml
junie-review:
  stage: review
  script:
    - curl -fsSL https://junie.jetbrains.com/install.sh | bash
    - junie --auth="$JUNIE_API_KEY" "마지막 커밋의 코드를 리뷰해줘"
  only:
    - merge_requests
```

### Headless 모드 주요 옵션

| 옵션 | 설명 |
|------|------|
| `--auth` | API 키 인증 |
| `--timeout` | 실행 시간 제한 (ms) |
| `--output-format` | 출력 형식 (`text`, `json`) |
| `--json-output-file` | JSON 결과를 파일로 저장 |
| `--model` | 사용할 LLM 모델 지정 |
| `--project` | 프로젝트 디렉토리 지정 |

### 활용 시나리오

1. **PR 자동 리뷰**: 풀 리퀘스트 생성 시 자동으로 코드 리뷰 실행
2. **코드 품질 게이트**: 머지 전 코드 품질 검사 수행
3. **테스트 자동 수정**: 실패한 테스트를 자동으로 분석/수정 시도
4. **문서 자동 생성**: 코드 변경에 따른 문서 자동 업데이트
5. **Merge Conflict 해결**: `--merge` 또는 `--rebase` 플래그 활용

```bash
# Merge Conflict 해결
junie --merge feature-branch

# Rebase Conflict 해결
junie --rebase main
```

---

## 로컬 코드 리뷰

`/review` 슬래시 커맨드를 사용하면 커밋 전에 로컬에서 코드 리뷰를 수행할 수 있습니다.

### 리뷰 대상 선택

`/review` 실행 시 현재 Git 상태에 따라 적절한 옵션이 제공됩니다:

| 옵션 | 설명 | 표시 조건 |
|------|------|----------|
| From Main | `main` 브랜치 대비 현재 브랜치 비교 | `main` 브랜치가 존재하고 현재 브랜치가 아닐 때 |
| Last Commit | 마지막 커밋의 diff 리뷰 | 항상 |
| Unstaged Changes | 스테이징되지 않은 변경사항 리뷰 | 미스테이징 변경사항이 있을 때 |

### 리뷰 결과 처리

리뷰 완료 후 발견된 이슈에 대해:
- **개별 코멘트 수락**: 제안된 수정사항 적용
- **코멘트 무시**: 불필요한 피드백 건너뛰기

> **참고**: 로컬 리뷰는 GitHub PR에서 실행되는 자동 코드 리뷰와 동일한 백엔드를 사용하므로, 리뷰 품질이 일관됩니다.

---

## 확장 기능

### MCP 서버 연동

Junie CLI에서도 MCP(Model Context Protocol) 서버를 사용할 수 있습니다:

```bash
# 추가 MCP 서버 위치 지정
junie --mcp-location /path/to/mcp-servers

# 기본 MCP 설정 위치
# ~/.junie/mcp.json (전역)
# <project>/.junie/mcp.json (프로젝트별)
```

> **참고**: Task 05에서 설정한 `.junie/mcp.json`은 CLI에서도 그대로 사용됩니다.

### Agent Skills

에이전트 스킬을 통해 Junie의 능력을 확장할 수 있습니다:

```bash
junie --skill-location ./my-skills
```

### Subagents

복잡한 작업을 여러 하위 에이전트로 분배하여 처리할 수 있습니다.

### 커스텀 가이드라인

프로젝트별 코딩 스타일이나 규칙을 가이드라인 파일로 제공:

```bash
# 기본: <project>/.junie/guidelines.md
# CI용 별도 가이드라인:
junie --guidelines-filename "guidelines_ci.md"
```

---

## 실전 프롬프트 예제

### 프로젝트 탐색 및 이해

```
> 이 프로젝트의 아키텍처와 주요 모듈을 설명해줘
> @src/main/java/ 패키지 의존성 구조를 분석해줘
> 데이터베이스 접근 계층이 어떻게 구현되어 있는지 설명해줘
```

### 코드 작성 및 리팩터링

```
> UserService에 이메일 중복 검사 기능을 추가해줘
> @src/main/java/legacy/OrderProcessor.java 이 클래스를 전략 패턴으로 리팩터링해줘
> REST API에 페이지네이션을 적용해줘
```

### 테스트 작성

```
> @src/main/java/PaymentService.java 에 대한 단위 테스트를 JUnit 5로 작성해줘
> 통합 테스트에서 외부 API 호출을 Mock으로 교체해줘
> 현재 실패하는 테스트를 분석하고 수정해줘
```

### 버그 수정 및 디버깅

```
> NullPointerException이 발생하는 경로를 추적하고 방어 코드를 추가해줘
> 동시성 이슈가 있는 코드를 찾아서 thread-safe하게 수정해줘
```

### CI/CD Headless 프롬프트 예제

```bash
# 코드 품질 검사
junie --auth="$KEY" "코드베이스에서 보안 취약점을 스캔하고 보고서를 생성해줘"

# 자동 수정
junie --auth="$KEY" "lint 경고를 자동으로 수정하고 테스트가 통과하는지 확인해줘"

# 문서 동기화
junie --auth="$KEY" "변경된 API에 대한 README와 Javadoc을 업데이트해줘"
```

---

## 문제 해결

### 설치 관련

| 문제 | 해결 방법 |
|------|----------|
| `junie: command not found` | 터미널 재시작 또는 PATH 확인 |
| 설치 스크립트 실행 권한 오류 | PowerShell 실행 정책 확인: `Set-ExecutionPolicy Bypass -Scope Process` |
| 네트워크 오류로 설치 실패 | 프록시 설정 확인 또는 수동 다운로드 |

### 인증 관련

| 문제 | 해결 방법 |
|------|----------|
| 브라우저 로그인 리디렉션 실패 | `/account`에서 재로그인 또는 API Key 방식 사용 |
| BYOK 키 인증 오류 | API 키 유효성 및 잔액 확인 |
| 토큰 만료 | [junie.jetbrains.com/cli](https://junie.jetbrains.com/cli)에서 새 토큰 발급 |

### 실행 관련

| 문제 | 해결 방법 |
|------|----------|
| 프로젝트 인식 실패 | `--project` 플래그로 명시적 경로 지정 |
| MCP 서버 연결 실패 | `.junie/mcp.json` 경로 및 설정 확인 |
| 모델 응답 없음 | `/model`로 다른 모델 선택 또는 `/usage`로 잔액 확인 |
| 명령 승인이 번거로움 | `~/.junie/allowlist.json`에 신뢰할 명령 추가 |

### Action Allowlist 설정

반복적으로 승인해야 하는 명령은 Allowlist에 추가할 수 있습니다:

1. 명령 승인 시 `→ Always allow` 옵션 선택
2. 또는 `~/.junie/allowlist.json` 직접 편집:

```json
{
  "allowed": [
    "gradlew test",
    "gradlew build",
    "npm test"
  ]
}
```

### 유용한 단축키 모음

| 단축키 | 기능 |
|--------|------|
| `Ctrl+R` | 프롬프트 히스토리 검색 |
| `Ctrl+B` | Brave 모드 토글 |
| `Ctrl+T` | 세션 전체 트랜스크립트 보기 |
| `Ctrl+N` (Transcript 뷰) | 이전 기록 로드 |
| `Shift+Tab` | Plan 모드 전환 |
| `Shift+Tab+Tab` | Debug 모드 전환 |
| `Ctrl+C` (2회) | Junie CLI 종료 |
| `?` | 모든 단축키 목록 보기 |

---

## 학습 체크리스트

- [ ] Junie CLI 설치 및 버전 확인 완료
- [ ] 인증 방법 선택 및 로그인 완료
- [ ] 인터랙티브 모드에서 기본 프롬프트 실행
- [ ] `@`로 파일 참조하여 컨텍스트 포함 실행
- [ ] Plan 모드로 작업 계획 수립 후 Code 모드로 실행
- [ ] `/review`로 로컬 코드 리뷰 수행
- [ ] 커스텀 슬래시 커맨드 1개 이상 생성
- [ ] BYOK로 원하는 LLM 연결 성공
- [ ] (선택) CI/CD 파이프라인에 Headless 모드 연동

---

## 참고 자료

- [Junie CLI 공식 문서](https://junie.jetbrains.com/docs/junie-cli.html)
- [CLI 파라미터 레퍼런스](https://junie.jetbrains.com/docs/parameters.html)
- [Headless 모드 가이드](https://junie.jetbrains.com/docs/junie-headless.html)
- [커스텀 슬래시 커맨드](https://junie.jetbrains.com/docs/custom-slash-commands.html)
- [MCP 설정 (CLI)](https://junie.jetbrains.com/docs/junie-cli-mcp-configuration.html)
- [모델 선택 가이드](https://junie.jetbrains.com/docs/junie-cli-model-selection.html)

---

> 💡 **다음 단계**: 이 가이드의 내용을 익힌 후, Task 06~10의 고급 작업을 Junie CLI의 Headless 모드를 활용하여 CI/CD에 자동화하는 것을 시도해 보세요!
