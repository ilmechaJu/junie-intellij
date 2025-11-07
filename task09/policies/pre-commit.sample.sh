#!/usr/bin/env bash
# Sample pre-commit hook (use with Husky or pre-commit frameworks accordingly)
# - Prevent committing secrets and TODOs
# - Run basic checks fast

set -euo pipefail

# 1) Secret pattern check (simple)
if git grep -nE "(AWS_SECRET_ACCESS_KEY|AKIA[0-9A-Z]{16}|-----BEGIN (RSA|EC) PRIVATE KEY-----)" -- . ':!task09/policies/pre-commit.sample.sh'; then
  echo "[SECURITY] Potential secret detected. Commit aborted." >&2
  exit 1
fi

# 2) Block TODO/FIXME in committed diffs (adjust as needed)
if git diff --cached | grep -E "\+.*(TODO|FIXME)"; then
  echo "[QUALITY] TODO/FIXME found in staged changes. Please resolve or suppress." >&2
  exit 1
fi

# 3) Fast checks (format/lint/test subset) – customize per project
# 프로젝트 빌드 도구를 자동 감지하여 빠른 테스트를 실행합니다.
if [ -x "./gradlew" ]; then
  ./gradlew -q test
elif [ -f "./gradlew.bat" ]; then
  ./gradlew.bat -q test
elif [ -x "./mvnw" ]; then
  ./mvnw -q -DskipTests=false test
elif command -v mvn >/dev/null 2>&1; then
  mvn -q -DskipTests=false test
elif [ -f "package.json" ] && command -v npm >/dev/null 2>&1; then
  npm test --silent --if-present
else
  echo "[INFO] No recognized build tool found; skipping tests"
fi
