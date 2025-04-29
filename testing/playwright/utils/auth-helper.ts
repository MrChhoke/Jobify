import { Page, BrowserContext } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';
import * as fs from 'fs';
import * as path from 'path';

/**
 * Authentication helper for managing login state
 */
export class AuthHelper {
  private static readonly STORAGE_STATE_DIR = path.join(__dirname, '..', 'fixtures');
  private static readonly STORAGE_FILE = 'auth.json';

  /**
   * Login and save authentication state
   * @param page - Playwright page
   * @param context - Playwright browser context
   * @param username - Username
   * @param password - Password
   * @param storageStateFileName - Optional custom filename for storage state
   */
  static async loginAndSaveState(
    page: Page,
    context: BrowserContext,
    username: string,
    password: string,
    storageStateFileName: string = AuthHelper.STORAGE_FILE
  ): Promise<void> {
    // Navigate to homepage
    await page.goto('/');

    // Click the login button to open the login modal
    console.log('[DEBUG_LOG] Clicking login button');
    await page.click('button:has-text("Вхід")');

    // Wait for the login modal to appear
    console.log('[DEBUG_LOG] Waiting for login modal');
    await page.waitForSelector('[data-testid="username-input"]', { timeout: 5000 });

    // Create login page object
    const loginPage = new LoginPage(page);

    // Login with credentials
    console.log(`[DEBUG_LOG] Logging in with username: ${username}`);
    await loginPage.loginWithRoleSelectors(username, password);

    // Ensure storage directory exists
    if (!fs.existsSync(AuthHelper.STORAGE_STATE_DIR)) {
      fs.mkdirSync(AuthHelper.STORAGE_STATE_DIR, { recursive: true });
    }

    // Save storage state
    await context.storageState({
      path: path.join(AuthHelper.STORAGE_STATE_DIR, storageStateFileName)
    });
  }

  /**
   * Get path to storage state file
   * @param storageStateFileName - Optional custom filename for storage state
   * @returns Full path to storage state file
   */
  static getStorageStatePath(storageStateFileName: string = AuthHelper.STORAGE_FILE): string {
    return path.join(AuthHelper.STORAGE_STATE_DIR, storageStateFileName);
  }

  /**
   * Check if storage state file exists
   * @param storageStateFileName - Optional custom filename for storage state
   * @returns True if storage state file exists
   */
  static storageStateExists(storageStateFileName: string = AuthHelper.STORAGE_FILE): boolean {
    return fs.existsSync(path.join(AuthHelper.STORAGE_STATE_DIR, storageStateFileName));
  }

  /**
   * Delete storage state file
   * @param storageStateFileName - Optional custom filename for storage state
   */
  static deleteStorageState(storageStateFileName: string = AuthHelper.STORAGE_FILE): void {
    const filePath = path.join(AuthHelper.STORAGE_STATE_DIR, storageStateFileName);
    if (fs.existsSync(filePath)) {
      fs.unlinkSync(filePath);
    }
  }

  /**
   * Create storage state files for different user roles
   * @param page - Playwright page
   * @param context - Playwright browser context
   */
  static async setupUserRoles(page: Page, context: BrowserContext): Promise<void> {
    // Regular user
    await AuthHelper.loginAndSaveState(
      page,
      context,
      'testuser',
      'password123',
      'user-auth.json'
    );

    // Admin user
    await AuthHelper.loginAndSaveState(
      page,
      context,
      'admin',
      'admin123',
      'admin-auth.json'
    );

    // Recruiter user
    await AuthHelper.loginAndSaveState(
      page,
      context,
      'recruiter',
      'recruiter123',
      'recruiter-auth.json'
    );
  }
}
