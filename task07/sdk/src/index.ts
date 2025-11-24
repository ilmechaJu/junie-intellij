import type { HealthResponse, ProblemDetails } from './types';

export interface ClientOptions {
  baseUrl?: string;
  timeoutMs?: number;
  retries?: number;
  retryDelayMs?: number;
}

export class ApiClient {
  private baseUrl: string;
  private timeoutMs: number;
  private retries: number;
  private retryDelayMs: number;

  constructor(opts: ClientOptions = {}) {
    this.baseUrl = (opts.baseUrl ?? 'http://localhost:8080').replace(/\/$/, '');
    this.timeoutMs = opts.timeoutMs ?? 5000;
    this.retries = opts.retries ?? 2;
    this.retryDelayMs = opts.retryDelayMs ?? 200;
  }

  async getHealth(): Promise<{ data: HealthResponse; requestId?: string } | never> {
    const res = await this.request('/health', { method: 'GET' });
    const data = (await res.json()) as HealthResponse;
    const requestId = res.headers.get('x-request-id') ?? undefined;
    return { data, requestId };
  }

  private async request(path: string, init: RequestInit): Promise<Response> {
    let attempt = 0;
    let lastError: any;
    const url = this.baseUrl + path;

    while (attempt <= this.retries) {
      const ac = new AbortController();
      const id = setTimeout(() => ac.abort(new Error('Request timeout')), this.timeoutMs);
      try {
        const res = await fetch(url, { ...init, signal: ac.signal, headers: { 'content-type': 'application/json', ...(init.headers || {}) } });
        clearTimeout(id);
        if (res.ok) return res;
        // Retry on 5xx
        if (res.status >= 500 && res.status < 600) {
          lastError = new Error(`Server error ${res.status}`);
        } else {
          const problem = (await safeJson(res)) as ProblemDetails | undefined;
          const err = new Error(`HTTP ${res.status}${problem?.title ? `: ${problem.title}` : ''}`);
          (err as any).problem = problem;
          throw err;
        }
      } catch (e) {
        lastError = e;
      }
      attempt++;
      if (attempt <= this.retries) {
        await delay(this.retryDelayMs * attempt);
      }
    }
    throw lastError ?? new Error('Unknown network error');
  }
}

async function safeJson(res: Response) {
  try {
    return await res.json();
  } catch {
    return undefined;
  }
}

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

export type { HealthResponse, ProblemDetails } from './types';
