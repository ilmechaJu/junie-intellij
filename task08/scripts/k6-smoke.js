import http from 'k6/http';
import { sleep, check } from 'k6';

// Environment-configurable options
const VUS = Number(__ENV.VUS || 10);
const DURATION = __ENV.DURATION || '30s';
const RATE = Number(__ENV.RATE || 0); // 0 means disabled
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const PATH = __ENV.PATH || '/health';
const FULL_URL = `${BASE_URL}${PATH}`;

export const options = RATE > 0 ? {
  scenarios: {
    constant_rate: {
      executor: 'constant-arrival-rate',
      rate: RATE, // requests per second
      timeUnit: '1s',
      duration: DURATION,
      preAllocatedVUs: Math.max(VUS, 10),
      maxVUs: Math.max(VUS * 2, 50),
    }
  },
  thresholds: {
    http_req_duration: [
      'p(95)<500',  // target p95 under 500ms
      'p(99)<1000', // target p99 under 1s
    ],
    http_req_failed: ['rate<0.01'], // <1% errors
  }
} : {
  vus: VUS,
  duration: DURATION,
  thresholds: {
    http_req_duration: ['p(95)<500','p(99)<1000'],
    http_req_failed: ['rate<0.01']
  }
};

export default function () {
  const res = http.get(FULL_URL);
  check(res, {
    'status is 2xx': (r) => r.status >= 200 && r.status < 300,
  });
  sleep(1);
}
