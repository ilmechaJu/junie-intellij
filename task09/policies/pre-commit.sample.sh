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
./gradlew.bat -q test
