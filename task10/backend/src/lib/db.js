import Database from 'better-sqlite3';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const dataDir = path.join(__dirname, '..', '..', 'data');
const dbPath = path.join(dataDir, 'app.db');

let _db;

export function initDb() {
  if (!fs.existsSync(dataDir)) fs.mkdirSync(dataDir, { recursive: true });
  _db = new Database(dbPath);
  _db.pragma('journal_mode = WAL');
  // Ensure base tables for first run (idempotent)
  _db.prepare(`CREATE TABLE IF NOT EXISTS todos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    completed INTEGER NOT NULL DEFAULT 0,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
  )`).run();
  // If missing due_date (post-migration feature), keep running without it
  const columns = _db.prepare("PRAGMA table_info('todos')").all();
  const hasDueDate = columns.some(c => c.name === 'due_date');
  if (!hasDueDate) {
    // no-op, migrations can add later
  }
}

export const db = new Proxy({}, {
  get(_t, prop) {
    if (!_db) initDb();
    return _db[prop].bind(_db);
  }
});

export function getDbPath() {
  return dbPath;
}
