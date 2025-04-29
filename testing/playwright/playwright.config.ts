import { defineConfig, devices } from '@playwright/test';
import * as path from "node:path";

export default defineConfig({
  testDir: './tests',
  outputDir: './test-results',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: "line",
  use: {
    baseURL: 'http://localhost:3000',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    headless: false,
    // Store any authentication state in the fixtures directory
    storageState: path.join(__dirname, 'fixtures', 'auth.json'),
  },
  projects: [
    // Setup project to create authentication state
    {
      name: 'setup',
      testMatch: /.*\.setup\.ts/,
    },
    // Main browser projects
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
      dependencies: ['setup'],
    }
  ],
  webServer: {
    command: 'cd ../../frontend && npm start',
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
    timeout: 120 * 1000, // 2 minutes
  },
});
