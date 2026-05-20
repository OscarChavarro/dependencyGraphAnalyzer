import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { BACKEND_BASE_URL } from './app/config-tokens';

interface SecretsFile {
  backend?: {
    url?: string;
  };
}

function normalizeBackendUrl(rawUrl: string): string {
  const trimmedUrl = rawUrl.trim();
  const portAsPathMatch = trimmedUrl.match(/^(https?:\/\/[^/]+)\/(\d{2,5})$/);
  if (portAsPathMatch) {
    return `${portAsPathMatch[1]}:${portAsPathMatch[2]}`;
  }
  return trimmedUrl.replace(/\/+$/, '');
}

async function bootstrap(): Promise<void> {
  let backendUrl = 'http://localhost:8080';

  try {
    const response = await fetch('/secrets.json', { cache: 'no-store' });
    if (response.ok) {
      const secrets = (await response.json()) as SecretsFile;
      if (secrets.backend?.url) {
        backendUrl = normalizeBackendUrl(secrets.backend.url);
      }
    }
  } catch (error) {
    console.warn('Could not load /secrets.json, using default backend URL.', error);
  }

  await bootstrapApplication(App, {
    ...appConfig,
    providers: [
      ...(appConfig.providers ?? []),
      { provide: BACKEND_BASE_URL, useValue: backendUrl }
    ]
  });
}

bootstrap().catch((err) => console.error(err));
