# CI-Security 예시(초보자 가이드)

이 디렉토리는 보안 파이프라인을 빠르게 도입하기 위한 템플릿을 제공합니다. 템플릿은 바로 동작 가능한 수준의 예시이며, 조직 표준에 맞게 수정해 사용하세요.

포함 내용
- SAST: 코드 정적 분석(예: SpotBugs, ESLint 등)
- DAST: 배포/샌드박스 대상 동적 스캔(예: OWASP ZAP Baseline)
- Dependency Audit + SBOM: 취약 의존성 검사(OWASP Dependency-Check) 및 SBOM(CycloneDX)
- Secret Scanning: `gitleaks` 기반 시크릿 탐지

파일 설명
- `github-actions.security.yml` → GitHub Actions 템플릿(이 파일을 `.github/workflows/security.yml`로 복사)
- `gitleaks.toml` → gitleaks 기본 설정(허용리스트/제외 규칙 포함 샘플)
- `zap-rules.tsv` → ZAP Baseline에서 규칙 심각도 조정 샘플

빠른 적용 방법
1) 사용하는 CI 플랫폼 확인(GitHub)
2) 해당 템플릿 파일을 프로젝트 루트로 복사
   - GitHub: `.github/workflows/security.yml`
3) 커밋/푸시하면 보안 파이프라인이 자동 실행됩니다.
 
로컬에서 테스트하기
- Secret Scan: `gitleaks detect --source . --redact`
- Dependency Audit (Gradle 예): `./gradlew dependencyCheckAnalyze cyclonedxBom`
- ZAP Baseline (Docker):
  - 애플리케이션을 `http://localhost:8080`에서 가동
  - `docker run --rm -t owasp/zap2docker-stable zap-baseline.py -t http://localhost:8080 -r zap.html -w ci-security/zap-rules.tsv || true`

결과 해석과 후속 조치
- High/Critical 이슈는 즉시 이슈 트래커에 등록하고, 예외가 필요한 경우 근거를 갖춘 승인/만료일을 문서화하세요.
- 반복 발생 항목은 규칙/정책으로 고도화하여 CI에서 자동 차단하거나 경고를 최소화하세요.
