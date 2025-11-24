import { createApp } from './app';

const port = Number(process.env.PORT || 8080);
const app = createApp();

app.listen(port, () => {
  // eslint-disable-next-line no-console
  console.log(`[server] listening on http://localhost:${port}`);
});
