import express from 'express';
import cors from 'cors';
import morgan from 'morgan';
import path from 'path';
import { fileURLToPath } from 'url';
import client from 'prom-client';
import { db, initDb } from './lib/db.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Init DB (ensure file exists)
initDb();

const app = express();
app.use(cors());
app.use(express.json());
app.use(morgan('dev'));

// Metrics
const collectDefaultMetrics = client.collectDefaultMetrics;
collectDefaultMetrics();

const httpRequestDuration = new client.Histogram({
  name: 'http_request_duration_seconds',
  help: 'HTTP request duration in seconds',
  labelNames: ['method', 'route', 'code'],
  buckets: [0.05, 0.1, 0.3, 0.5, 1, 2, 5]
});
const httpRequestsTotal = new client.Counter({
  name: 'http_requests_total',
  help: 'Total number of HTTP requests',
  labelNames: ['method', 'route', 'code']
});

app.use((req, res, next) => {
  const end = httpRequestDuration.startTimer();
  res.on('finish', () => {
    const labels = { method: req.method, route: req.route?.path || req.path, code: res.statusCode };
    end(labels);
    httpRequestsTotal.inc(labels);
  });
  next();
});

// Health
app.get('/healthz', (_req, res) => {
  res.json({ ok: true });
});

// Metrics endpoint
app.get('/metrics', async (_req, res) => {
  res.set('Content-Type', client.register.contentType);
  res.end(await client.register.metrics());
});

// API routes
app.get('/api/todos', (_req, res) => {
  const rows = db.prepare('SELECT id, title, due_date as dueDate, completed, created_at as createdAt, updated_at as updatedAt FROM todos ORDER BY created_at DESC').all();
  res.json(rows);
});

app.post('/api/todos', (req, res) => {
  const { title, dueDate } = req.body || {};
  if (!title || typeof title !== 'string') {
    return res.status(400).json({ error: 'title is required' });
  }
  const now = new Date().toISOString();
  const result = db.prepare('INSERT INTO todos (title, due_date, completed, created_at, updated_at) VALUES (?, ?, 0, ?, ?)')
    .run(title, dueDate || null, now, now);
  const row = db.prepare('SELECT id, title, due_date as dueDate, completed, created_at as createdAt, updated_at as updatedAt FROM todos WHERE id = ?').get(result.lastInsertRowid);
  res.status(201).json(row);
});

app.patch('/api/todos/:id/complete', (req, res) => {
  const { id } = req.params;
  const now = new Date().toISOString();
  const info = db.prepare('UPDATE todos SET completed = 1, updated_at = ? WHERE id = ?').run(now, id);
  if (info.changes === 0) return res.status(404).json({ error: 'not found' });
  const row = db.prepare('SELECT id, title, due_date as dueDate, completed, created_at as createdAt, updated_at as updatedAt FROM todos WHERE id = ?').get(id);
  res.json(row);
});

// Serve frontend static files
app.use('/', express.static(path.join(__dirname, '..', 'public')));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  // eslint-disable-next-line no-console
  console.log(`[backend] listening on http://localhost:${PORT}`);
});
