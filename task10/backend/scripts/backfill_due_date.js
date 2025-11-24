#!/usr/bin/env node
import { db, initDb } from '../src/lib/db.js';

initDb();

// Backfill strategy: if due_date is null, set it to created_at + 7 days
function backfill() {
  const haveColumn = db.prepare("PRAGMA table_info('todos')").all().some(c => c.name === 'due_date');
  if (!haveColumn) {
    console.log('[backfill] skipped: due_date column not found. Run migrations first.');
    return;
  }
  const rows = db.prepare('SELECT id, created_at, due_date FROM todos WHERE due_date IS NULL').all();
  let updated = 0;
  const addDays = (iso, days) => new Date(new Date(iso).getTime() + days*24*60*60*1000).toISOString();
  const stmt = db.prepare('UPDATE todos SET due_date = ?, updated_at = ? WHERE id = ?');
  for (const r of rows) {
    const due = addDays(r.created_at, 7);
    const now = new Date().toISOString();
    stmt.run(due, now, r.id);
    updated++;
  }
  console.log(`[backfill] updated ${updated} rows`);
}

backfill();
