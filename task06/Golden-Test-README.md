# Golden 테스트 가이드 (tests/golden)

이 디렉터리는 “골든 테스트(golden/snapshot test)” 케이스들을 보관합니다. 아래 지침에 따라 케이스를 추가하면 CI가 자동으로 컴파일 후 러너(GoldenTestRunner)를 실행하고, 기대 결과(golden)와 실제 결과를 비교해 성공/실패를 판단합니다.

주의: 현재 GoldenTestRunner는 플레이스홀더로 구현되어 있어 실제 비교를 수행하지 않습니다. 이 문서는 러너를 확장할 때 기준이 되는 요구사항을 정리합니다.


## 1) 디렉터리 구조
프로젝트 루트 기준(예시):

```
project-root/
  tests/
    golden/
      case-01/
        input.txt
        expected.txt   # 또는 expected.json 등, 포맷은 팀 합의에 따릅니다
      case-02/
        input.txt
        expected.txt
```

- 각 케이스는 폴더 하나로 구성합니다.
- 파일 명은 최소한 `input.txt`와 `expected.txt`를 권장합니다. 다른 포맷을 사용할 경우 러너의 파싱 로직을 함께 맞춰 주세요.
- 필요 시 실제 테스트 대상 기능을 별도의 자바 메서드로 분리하여 러너에서 호출 가능하도록 만듭니다.


## 2) 러너의 케이스 “발견” 규칙(구현 지침)
- 루트: `tests/golden`
- 동작:
  - `tests/golden` 하위의 모든 1단계 폴더를 정렬된 순서로 순회합니다.
  - 각 폴더에서 `input.txt`, `expected.txt` 존재 여부를 확인합니다.
  - `input.txt`를 읽어 테스트 대상 로직을 호출하여 실제 출력(actual)을 생성합니다.


## 3) 스냅샷(골든) 비교 규칙(구현 지침)
- 실제 출력(actual)과 기대 출력(expected)을 동일 포맷/정규화 규칙으로 비교합니다.
- 불일치 시:
  - 상세 차이를 `System.err.println`으로 출력합니다.
  - 프로세스를 비정상 종료(`System.exit(1)`)합니다.
- 일치 시:
  - 해당 케이스를 통과로 카운트합니다.


## 4) 종료 코드 규칙(구현 지침)
- 모든 케이스 통과: 정상 종료(종료 코드 0)
- 하나라도 실패: 비정상 종료(종료 코드 1)

이 규칙을 지키면 CI에서 성공/실패가 자동으로 반영됩니다.


## 5) CI 연동
- 현재 저장소의 CI(`.github/workflows/ci-example.yml`)는 다음을 수행합니다:
  - 모든 자바 소스를 `javac`로 컴파일하여 `out` 디렉터리에 생성
  - `java -cp out com.example.tests.golden.GoldenTestRunner` 실행
- 러너가 위의 종료 코드 규칙을 따르면, 골든 비교 결과가 CI 상태에 그대로 반영됩니다.


## 6) 로컬 실행 방법(참고)
GitHub Actions와 동일한 흐름을 로컬에서도 재현할 수 있습니다.

PowerShell 예시:

```
# 프로젝트 루트에서 실행
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out @sources.txt
java -cp out com.example.tests.golden.GoldenTestRunner
```

현재 러너는 플레이스홀더라 항상 성공합니다. 러너를 확장한 후에는 케이스 추가에 따라 성공/실패가 달라집니다.


## 7) 러너 확장 예시(요약 가이드)
아래는 구현시 고려할 핵심 포인트입니다.

- tests/golden 존재 확인 → 없으면 "통과(스킵)" 처리 가능
- 디렉터리 순회: `Files.list(GOLDEN_ROOT)`로 하위 폴더 정렬 순서대로
- 각 케이스 처리 흐름:
  1) `input.txt`/`expected.txt` 존재 확인
  2) 입력 읽기 → 대상 로직 호출 → actual 생성
  3) `expected`와 비교 → 불일치 시 상세 출력 후 실패 카운트 증가
- 전체 결과 출력: `total`, `passed`, `failed`
- 하나라도 실패했다면 `System.exit(1)` 호출


## 8) 권장 케이스 작성 팁
- 개행/공백 차이로 인한 오탐을 줄이려면 트림(trim) 또는 정규화 규칙을 러너/케이스에 명시하세요.
- JSON 등 구조화 포맷 비교 시 정렬/정규화(예: 키 정렬)를 고려하세요.
- 케이스 이름은 의도를 드러내는 짧은 식별자 사용(예: `upper-basic`, `edge-empty-input`).


## 9) 책임 범위
- 본 디렉터리는 “케이스와 스냅샷”을 보관합니다.
- 러너(`src/main/java/com/example/tests/golden/GoldenTestRunner.java`)는 발견/비교/종료 코드를 담당합니다.
- 빌드와 실행은 CI 워크플로우가 담당합니다.
