import { readFileSync } from 'fs';
import path from 'path';
import YAML from 'yaml';
import SwaggerParser from '@apidevtools/swagger-parser';
import Ajv from 'ajv';
import addFormats from 'ajv-formats';
import request from 'supertest';
import { createApp } from '../../server/src/app';

async function main() {
  const specPath = path.resolve(__dirname, '../../spec/openapi.yaml');
  console.log(`[contract] Spec: ${specPath}`);

  // 1) Spec schema validation
  await SwaggerParser.validate(specPath);
  console.log('[contract] OpenAPI spec is valid');

  const raw = readFileSync(specPath, 'utf8');
  const doc = YAML.parse(raw);

  // 2) Extract HealthResponse schema for response body validation
  const healthSchema = doc.components?.schemas?.HealthResponse;
  if (!healthSchema) throw new Error('HealthResponse schema missing in spec');

  const ajv = new Ajv({ allErrors: true, strict: false });
  addFormats(ajv);
  const validateHealth = ajv.compile(healthSchema);

  // 3) Spin up app and perform requests
  const app = createApp();

  // Happy path: GET /health
  const res = await request(app).get('/health').set('x-request-id', 'test-req-1').expect(200);

  // Validate header according to spec
  const requestId = res.headers['x-request-id'];
  if (!requestId) throw new Error('Missing x-request-id header on 200 response');

  // Validate body against spec
  const valid = validateHealth(res.body);
  if (!valid) {
    console.error(validateHealth.errors);
    throw new Error('Response body does not match HealthResponse schema');
  }

  // Negative path: unknown route -> 404 ProblemDetails
  const res404 = await request(app).get('/not-exist').expect(404);
  const problem = res404.body;
  const hasProblemShape = typeof problem === 'object' && problem && 'status' in problem && 'title' in problem;
  if (!hasProblemShape) throw new Error('404 does not return ProblemDetails shape');

  console.log('[contract] All checks passed (drift: 0)');
}

main().catch((err) => {
  console.error('[contract] FAILED:', err);
  process.exit(1);
});
