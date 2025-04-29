import { Page } from '@playwright/test';

/**
 * Base page class that all page objects will extend
 * Contains common methods and properties for all pages
 */
export class BasePage {
  readonly page: Page;
  readonly baseURL: string;

  /**
   * @param page - Playwright page object
   * @param baseURL - Base URL of the application
   */
  constructor(page: Page, baseURL: string = 'http://localhost:3000') {
    this.page = page;
    this.baseURL = baseURL;
  }

  /**
   * Navigate to a specific path
   * @param path - Path to navigate to
   */
  async navigate(path: string = ''): Promise<void> {
    await this.page.goto(`${this.baseURL}${path}`);
  }

  /**
   * Wait for navigation to complete
   */
  async waitForNavigation(): Promise<void> {
    await this.page.waitForLoadState('networkidle');
  }

  /**
   * Get page title
   * @returns Page title
   */
  async getTitle(): Promise<string> {
    return await this.page.title();
  }

  /**
   * Check if element is visible
   * @param selector - Element selector
   * @returns True if element is visible
   */
  async isVisible(selector: string): Promise<boolean> {
    const element = this.page.locator(selector);
    return await element.isVisible();
  }

  /**
   * Wait for element to be visible
   * @param selector - Element selector
   * @param timeout - Timeout in milliseconds
   */
  async waitForElement(selector: string, timeout: number = 5000): Promise<void> {
    await this.page.waitForSelector(selector, { timeout });
  }

  /**
   * Get text of element
   * @param selector - Element selector
   * @returns Text content of element
   */
  async getText(selector: string): Promise<string> {
    const element = this.page.locator(selector);
    return await element.textContent() || '';
  }

  /**
   * Click on element
   * @param selector - Element selector
   */
  async click(selector: string): Promise<void> {
    await this.page.click(selector);
  }

  /**
   * Fill input field
   * @param selector - Input selector
   * @param value - Value to fill
   */
  async fill(selector: string, value: string): Promise<void> {
    await this.page.fill(selector, value);
  }
}