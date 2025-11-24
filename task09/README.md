# Task 09 — 보안 하드닝: 위협 모델링, 정적/동적 분석, 시크릿 스캐닝

이 태스크는 애플리케이션의 보안을 체계적으로 강화하기 위한 최소 단위를 제공합니다. 초보자도 그대로 따라 할 수 있도록 템플릿과 체크리스트, 파이프라인 예시를 함께 제공합니다.

목표
- 공격 표면 정리, 취약점 점검/완화, DevSecOps 체인 구성

체크리스트(측정 가능한 완료 기준)
- [ ] 입력 검증 강화 및 위험 경로 테스트
  - SAST 경고(High/Critical) 0개 또는 모두 이슈화/예외 승인 문서화
  - 보안 단위/통합 테스트에 악성 페이로드 케이스 ≥ N개 추가(XSS/SQLi/경로 조작/SSRF 등)
  - DAST 주기 실행 결과 High/Critical 0개
- [ ] 취약 라이브러리 업데이트 및 호환성 검증
  - SBOM 생성 및 Dependency Audit 결과 High/Critical 0개
  - 업데이트 후 회귀 테스트 전부 통과
- [ ] 시크릿 유출 방지 정책 적용
  - 로컬 pre-commit 훅(또는 Husky/pre-commit) 및 CI에서 gitleaks 실행
  - 과거 커밋 히스토리 1회 스캔/정리 + allowlist/rules 저장

디렉토리
- /task09/threat-model/           → STRIDE 기반 위협 모델 문서(DFD/신뢰경계/매핑 포함)
- /task09/ci-security/            → CI 보안 파이프라인 템플릿 및 도구 설정 샘플
- /task09/policies/               → pre-commit 훅(셸/파워셸), 정책 샘플

빠른 시작(초보자 가이드)
1) 위협 모델부터 작성하기
- `threat-model/STRIDE-example.md`를 열어 DFD와 신뢰 경계를 자신의 서비스에 맞춰 수정합니다.
- 각 흐름/노드에 대해 S/T/R/I/D/E 위험과 완화책을 간단 표로 적습니다.

2) CI 보안 파이프라인 적용하기
- 사용하는 플랫폼에 맞는 템플릿을 복사합니다.
  - GitHub Actions: `ci-security/github-actions.security.yml` → 프로젝트 루트의 `.github/workflows/security.yml`
- 커밋 후 PR/파이프라인이 자동으로 SAST/Dependency/Secret/DAST 를 실행합니다.

3) 시크릿 유출 방지 훅 설치하기(선택 필수)
- Unix/macOS: `policies/pre-commit.sample.sh`를 프로젝트 루트의 `.git/hooks/pre-commit`으로 복사하고 실행권한을 부여합니다.
- Windows: `policies/pre-commit.sample.ps1`를 `.git/hooks/pre-commit`에 복사하고 PowerShell 실행 정책을 확인합니다.
- Node/Husky 또는 `pre-commit` 프레임워크를 쓰는 경우 해당 문서를 참고해 스크립트를 연결하세요.

4) 로컬에서 한 번 점검해보기
- Secret Scan: gitleaks 설치 후 `gitleaks detect --source . --redact` 실행
- 의존성 점검: Gradle/Maven/NPM에 맞춰 문서대로 실행
- DAST(ZAP Baseline): 템플릿과 `ci-security/zap-rules.tsv`를 참고해 기본 스캔

문서/예시
- threat-model/: STRIDE 기반 위협 모델 문서(DFD, 신뢰경계, 매핑, 악용사례)
- ci-security/: SAST/DAST/Dependency Audit/SBOM/Secret Scan 파이프라인 예시 + gitleaks/zap 설정
- policies/: pre-commit 훅(셸/파워셸), 시크릿 차단 샘플

주의
- 이 디렉토리의 템플릿은 즉시 실행 가능한 "예시"입니다. 실제 조직 표준과 빌드 도구에 맞춰 경로/옵션을 조정하십시오.
