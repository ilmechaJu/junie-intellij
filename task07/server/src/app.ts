import express, { Request, Response, NextFunction } from 'express';
import morgan from 'morgan';
import { middleware } from 'express-openapi-validator';
import path from 'path';

const SPEC_PATH = path.resolve(__dirname, '../../../spec/openapi.yaml');

export function createApp() {
  const app = express();
  app.use(express.json());
  app.use(morgan('dev'));

  // Correlation id header set
  app.use((req: Request, res: Response, next: NextFunction) => {
    const reqId = (req.headers['x-request-id'] as string) || cryptoRandom();
    res.setHeader('x-request-id', reqId);
    (req as any).requestId = reqId;
    next();
  });

  // OpenAPI request/response validation
  app.use(
    middleware({ apiSpec: SPEC_PATH, validateRequests: true, validateResponses: true })
  );

  // Routes
  app.get('/health', (_req, res) => {
    res.json({ status: 'ok' });
  });

  // Not found handler
  app.use((req, res) => {
    res.status(404).type('application/problem+json').json(problem(404, 'Not Found', `No route for ${req.method} ${req.path}`));
  });

  // Error handler
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  app.use((err: any, req: Request, res: Response, _next: NextFunction) => {
    const status = err.status || err.statusCode || 500;
    const title = err.message || 'Internal Server Error';
    const body = problem(status, title, err.path || undefined);
    if (status >= 500) {
      res.setHeader('Retry-After', '5');
    }
    res.status(status).type('application/problem+json').json(body);
  });

  return app;
}

function cryptoRandom() {
  // simple fallback random string
  return Math.random().toString(16).slice(2) + Date.now().toString(16);
}

function problem(status: number, title: string, instance?: string) {
  return {
    type: 'about:blank',
    title,
    status,
    detail: title,
    instance: instance || ''
  };
}
