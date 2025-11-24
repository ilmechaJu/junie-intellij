# Backend (Task10)

Stack
- Node.js (Express)
- SQLite (better-sqlite3)
- Prometheus client metrics

Run
- Install dependencies: `cd backend && npm i`
- Start server: `npm start`
- Default port: `3000`

Database
- File: `backend/data/app.db` (auto-created)
- Migrations:
  - Apply: `npm run migrate`
  - Rollback last: `npm run rollback`
- Backfill: `npm run backfill` (sets `due_date = created_at + 7d` for nulls)

API
- GET `/api/todos` → list
- POST `/api/todos` → create `{ title: string, dueDate?: ISO8601 }`
- PATCH `/api/todos/:id/complete` → mark complete
- Health: `/healthz`
- Metrics: `/metrics`

Zero-downtime approach (example)
1) Deploy code that can work with/without `due_date` column (nullable, optional usage)
2) Run migration `002_add_due_date.sql` (adds nullable column)
3) Backfill existing rows (optional or async)
4) Start using `due_date` in features and dashboards
5) Make `due_date` non-nullable later if needed (separate migration with guards)

Notes
- `prom-client` exposes default process metrics and `http_request_duration_seconds` histogram.
- Static frontend is served from `backend/public/`.
