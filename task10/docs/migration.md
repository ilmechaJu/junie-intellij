# 마이그레이션 계획 (Zero-Downtime)

대상: `todos` 테이블에 `due_date` 컬럼 추가 및 백필

사전 조건
- 애플리케이션이 `due_date` 컬럼 유무에 관계없이 동작해야 함(코드 방어 로직 반영됨)

단계
1) 선배포(코드 먼저)
   - 새 코드는 `due_date` 미존재 환경에서도 정상 동작
2) 마이그레이션 적용
   - `cd backend && npm run migrate`
   - 적용 순서 예시
     - 001_init.sql (최초 테이블 생성)
     - 002_add_due_date.sql (nullable 컬럼 추가)
3) 데이터 백필
   - `npm run backfill`
   - 전략: `due_date`가 NULL인 경우 `created_at + 7일`로 설정
4) 검증
   - 앱 헬스 확인: `GET /healthz`
   - 기능 확인: 생성/목록/완료
   - 메트릭 확인: `GET /metrics`에 요청 히스토그램/카운터 노출
5) 롤백 계획
   - 최근 배치 롤백: `npm run rollback`
   - 주의: 롤백 시 `due_date` 컬럼 제거가 포함될 수 있으므로, 코드가 컬럼 미존재도 처리 가능해야 함(이미 처리됨)

리스크와 가드
- 장시간 DDL 락: SQLite 단일 파일 DB로 리스크 낮음. 대규모 DB라면 배치 윈도우 고려.
- 백필 시간: 데이터량에 따라 분할/배치 수행 고려.
- 애플리케이션 호환성: 컬럼을 NULL 허용으로 추가하여 점진 적용.
