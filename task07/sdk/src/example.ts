import { ApiClient } from './index';

async function main() {
  const client = new ApiClient({ baseUrl: 'http://localhost:8080' });
  const { data, requestId } = await client.getHealth();
  // eslint-disable-next-line no-console
  console.log('health:', data, 'x-request-id:', requestId);
}

main().catch((e) => {
  // eslint-disable-next-line no-console
  console.error('SDK example failed:', e);
  process.exit(1);
});
