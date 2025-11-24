#!/usr/bin/env pwsh
# Sample pre-commit hook (PowerShell)
# - Prevent committing secrets and TODOs
# - Run basic checks fast (auto-detect build tool)

$ErrorActionPreference = 'Stop'

Write-Host "[HOOK] pre-commit (PowerShell) running..."

# 1) Secret pattern check (simple). Exclude this file itself.
$secretPattern = '(AWS_SECRET_ACCESS_KEY|AKIA[0-9A-Z]{16}|-----BEGIN (RSA|EC) PRIVATE KEY-----)'
$null = git grep -nE $secretPattern -- . ':!task09/policies/pre-commit.sample.ps1'
if ($LASTEXITCODE -eq 0) {
  Write-Error "[SECURITY] Potential secret detected. Commit aborted."
}

# 2) Block TODO/FIXME in staged diffs
$diff = git diff --cached | Select-String -Pattern "\+.*(TODO|FIXME)"
if ($diff) {
  Write-Error "[QUALITY] TODO/FIXME found in staged changes. Please resolve or suppress."
}

# 3) Fast checks – auto-detect common build tools
if (Test-Path ./gradlew.bat) {
  Write-Host "[HOOK] Running Gradle tests..."
  & ./gradlew.bat -q test
} elseif (Test-Path ./gradlew) {
  Write-Host "[HOOK] Running Gradle tests (Unix wrapper via WSL/Git Bash may be required)..."
  & ./gradlew -q test
} elseif (Test-Path ./mvnw.cmd) {
  Write-Host "[HOOK] Running Maven tests..."
  & ./mvnw -q -DskipTests=false test
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
  Write-Host "[HOOK] Running Maven tests (system mvn)..."
  mvn -q -DskipTests=false test
} elseif (Test-Path ./package.json -and (Get-Command npm -ErrorAction SilentlyContinue)) {
  Write-Host "[HOOK] Running npm test..."
  npm test --silent --if-present
} else {
  Write-Host "[INFO] No recognized build tool found; skipping tests"
}

Write-Host "[HOOK] pre-commit checks passed."