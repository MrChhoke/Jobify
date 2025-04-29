import { Page } from '@playwright/test';
import { BasePage } from './BasePage';

/**
 * Page object for the login page
 * Handles authentication-related actions
 */
export class LoginPage extends BasePage {
  // Selectors
  readonly usernameInput = '[data-testid="username-input"]';
  readonly passwordInput = '[data-testid="password-input"]';
  readonly loginButton = '[data-testid="login-button"]';
  readonly registerLink = '[data-testid="forgot-password-link"]';
  readonly errorMessage = '[data-testid="error-message"]';

  /**
   * @param page - Playwright page object
   */
  constructor(page: Page) {
    super(page);
  }

  /**
   * Navigate to login page
   */
  async goto(): Promise<void> {
    await this.navigate('/login');
  }

  /**
   * Login with username and password
   * @param username - Username
   * @param password - Password
   */
  async login(username: string, password: string): Promise<void> {
    await this.fill(this.usernameInput, username);
    await this.fill(this.passwordInput, password);
    await this.click(this.loginButton);
    await this.waitForNavigation();
  }

  /**
   * Get error message text
   * @returns Error message text
   */
  async getErrorMessage(): Promise<string> {
    return await this.getText(this.errorMessage);
  }

  /**
   * Check if error message is visible
   * @returns True if error message is visible
   */
  async isErrorVisible(): Promise<boolean> {
    return await this.isVisible(this.errorMessage);
  }

  /**
   * Navigate to registration page
   */
  async navigateToRegister(): Promise<void> {
    await this.click(this.registerLink);
    await this.waitForNavigation();
  }

  /**
   * Alternative selectors using data-testid attributes for more resilient tests
   */
  async loginWithRoleSelectors(username: string, password: string): Promise<void> {
    await this.page.locator('[data-testid="username-input"]').fill(username);
    await this.page.locator('[data-testid="password-input"]').fill(password);
    await this.page.locator('[data-testid="login-button"]').click();
    await this.waitForNavigation();
  }
}
