#!/usr/bin/env node
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { db, getDbPath, initDb } from '../src/lib/db.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const migrationsDir = path.join(__dirname, '..', 'migrations');
const migrationsTable = '__migrations';

function ensureMigrationsTable() {
  db.prepare(`CREATE TABLE IF NOT EXISTS ${migrationsTable} (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    run_at TEXT NOT NULL
  )`).run();
}

function listMigrationFiles() {
  if (!fs.existsSync(migrationsDir)) return [];
  return fs.readdirSync(migrationsDir)
    .filter(f => f.endsWith('.sql'))
    .sort();
}

function getAppliedNames() {
  ensureMigrationsTable();
  return db.prepare(`SELECT name FROM ${migrationsTable} ORDER BY name`).all().map(r => r.name);
}

function up() {
  initDb();
  ensureMigrationsTable();
  const files = listMigrationFiles();
  const applied = new Set(getAppliedNames());
  const toApply = files.filter(f => !applied.has(f));
  for (const f of toApply) {
    const sql = fs.readFileSync(path.join(migrationsDir, f), 'utf8');
    const now = new Date().toISOString();
    db.exec('BEGIN');
    try {
      db.exec(sql);
      db.prepare(`INSERT INTO ${migrationsTable} (name, run_at) VALUES (?, ?)`).run(f, now);
      db.exec('COMMIT');
      console.log(`[migrate] applied ${f}`);
    } catch (e) {
      db.exec('ROLLBACK');
      console.error(`[migrate] failed ${f}:`, e.message);
      process.exitCode = 1;
      return;
    }
  }
  if (toApply.length === 0) console.log('[migrate] no pending migrations');
}

function down() {
  initDb();
  ensureMigrationsTable();
  const last = db.prepare(`SELECT name FROM ${migrationsTable} ORDER BY name DESC LIMIT 1`).get();
  if (!last) {
    console.log('[rollback] nothing to rollback');
    return;
  }
  const name = last.name;
  const downFile = name.replace('.sql', '.down.sql');
  const downPath = path.join(migrationsDir, downFile);
  if (!fs.existsSync(downPath)) {
    console.error(`[rollback] missing down file for ${name}: ${downFile}`);
    process.exitCode = 1;
    return;
  }
  const sql = fs.readFileSync(downPath, 'utf8');
  db.exec('BEGIN');
  try {
    db.exec(sql);
    db.prepare(`DELETE FROM ${migrationsTable} WHERE name = ?`).run(name);
    db.exec('COMMIT');
    console.log(`[rollback] reverted ${name}`);
  } catch (e) {
    db.exec('ROLLBACK');
    console.error(`[rollback] failed ${name}:`, e.message);
    process.exitCode = 1;
  }
}

const cmd = process.argv[2] || 'up';
if (cmd === 'up') up();
else if (cmd === 'down') down();
else {
  console.error('Usage: node scripts/migrate.js [up|down]');
  process.exit(1);
}
