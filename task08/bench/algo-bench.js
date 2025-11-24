#!/usr/bin/env node
/**
 * Microbenchmark: Top-K frequent elements (naive vs optimized)
 * - Runs multiple iterations to produce latency distribution (p50/p95/p99)
 * - Tracks rough peak RSS memory observed during the run
 * - Saves JSON results under bench/results/
 *
 * Usage (Windows PowerShell):
 *   node --expose-gc .\bench\algo-bench.js --n=200000 --k=25 --it=30
 *
 * Options:
 *   --n   Number of items in the generated array (default: 100000)
 *   --k   K (top-k count) (default: 10)
 *   --it  Iterations per variant (default: 20)
 */

const fs = require('fs');
const path = require('path');

function parseArgs() {
  const args = process.argv.slice(2);
  const opts = { n: 100000, k: 10, it: 20 };
  for (const a of args) {
    const m = a.match(/^--(n|k|it)=(\d+)$/);
    if (m) {
      opts[m[1]] = parseInt(m[2], 10);
    }
  }
  return opts;
}

// Utility: random integer in [0, max)
function randInt(max) {
  return Math.floor(Math.random() * max);
}

// Generate array with a skewed distribution (Zipf-ish by buckets)
function generateData(n) {
  const arr = new Array(n);
  for (let i = 0; i < n; i++) {
    // 70% from small id range, 25% medium, 5% large
    const r = Math.random();
    if (r < 0.70) arr[i] = randInt(1000);
    else if (r < 0.95) arr[i] = 1000 + randInt(9000);
    else arr[i] = 10000 + randInt(90000);
  }
  return arr;
}

// Naive: count then sort entries by count desc (O(U log U))
function topKNaive(arr, k) {
  const freq = new Map();
  for (const v of arr) freq.set(v, (freq.get(v) || 0) + 1);
  return Array.from(freq.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, k)
    .map(e => e[0]);
}

// Optimized: bucket sort by frequency (O(N + U))
function topKBucket(arr, k) {
  const freq = new Map();
  for (const v of arr) freq.set(v, (freq.get(v) || 0) + 1);
  const maxFreq = Math.max(...freq.values());
  const buckets = Array.from({ length: maxFreq + 1 }, () => []);
  for (const [val, f] of freq.entries()) buckets[f].push(val);
  const out = [];
  for (let f = maxFreq; f >= 0 && out.length < k; f--) {
    const bucket = buckets[f];
    if (bucket && bucket.length) {
      // If needed, trim bucket to remaining slots
      for (const v of bucket) {
        out.push(v);
        if (out.length === k) break;
      }
    }
  }
  return out;
}

function nowNs() {
  return process.hrtime.bigint();
}

function nsToMs(ns) {
  return Number(ns) / 1e6;
}

function percentile(values, p) {
  if (!values.length) return 0;
  const sorted = [...values].sort((a, b) => a - b);
  const idx = Math.min(sorted.length - 1, Math.ceil((p / 100) * sorted.length) - 1);
  return sorted[idx];
}

function memoryRssMB() {
  return process.memoryUsage().rss / (1024 * 1024);
}

async function runVariant(name, fn, data, iters) {
  const timings = [];
  let peakMB = memoryRssMB();
  for (let i = 0; i < iters; i++) {
    if (global.gc) global.gc(); // requires --expose-gc
    const start = nowNs();
    fn(data, PARAMS.k);
    const end = nowNs();
    const durMs = nsToMs(end - start);
    timings.push(durMs);
    peakMB = Math.max(peakMB, memoryRssMB());
  }
  return {
    name,
    count: timings.length,
    p50: percentile(timings, 50),
    p95: percentile(timings, 95),
    p99: percentile(timings, 99),
    mean: timings.reduce((a, b) => a + b, 0) / timings.length,
    max: Math.max(...timings),
    min: Math.min(...timings),
    peakRssMB: peakMB,
  };
}

const PARAMS = parseArgs();

(async function main() {
  console.log('[algo-bench] params', PARAMS);
  console.log('[algo-bench] generating data...');
  const data = generateData(PARAMS.n);

  const baseline = await runVariant('naive-sort', topKNaive, data, PARAMS.it);
  const optimized = await runVariant('bucket-optimized', topKBucket, data, PARAMS.it);

  function pctImprovement(oldV, newV) {
    return ((oldV - newV) / oldV) * 100;
  }

  const summary = {
    meta: {
      timestamp: new Date().toISOString(),
      node: process.version,
      platform: process.platform,
      arch: process.arch,
      params: PARAMS,
      notes: 'Run with node --expose-gc for more stable memory measures.'
    },
    baseline,
    optimized,
    improvements: {
      p95_ms: pctImprovement(baseline.p95, optimized.p95),
      p99_ms: pctImprovement(baseline.p99, optimized.p99),
      mean_ms: pctImprovement(baseline.mean, optimized.mean),
      peak_rss_mb: pctImprovement(baseline.peakRssMB, optimized.peakRssMB),
    }
  };

  console.log('\n=== Result Summary ===');
  console.table({
    baseline_p95_ms: baseline.p95.toFixed(2),
    optimized_p95_ms: optimized.p95.toFixed(2),
    p95_improve_pct: summary.improvements.p95_ms.toFixed(2),
    baseline_p99_ms: baseline.p99.toFixed(2),
    optimized_p99_ms: optimized.p99.toFixed(2),
    p99_improve_pct: summary.improvements.p99_ms.toFixed(2),
    baseline_peakMB: baseline.peakRssMB.toFixed(1),
    optimized_peakMB: optimized.peakRssMB.toFixed(1),
    peak_improve_pct: summary.improvements.peak_rss_mb.toFixed(2),
  });

  // Persist result JSON
  try {
    const outDir = path.join(__dirname, 'results');
    fs.mkdirSync(outDir, { recursive: true });
    const file = path.join(
      outDir,
      `result-${Date.now()}.json`
    );
    fs.writeFileSync(file, JSON.stringify(summary, null, 2));
    console.log(`\nSaved detailed results to: ${file}`);
  } catch (e) {
    console.warn('Failed to save results file:', e.message);
  }
})();
