import { Page } from '@playwright/test';
import { BasePage } from './BasePage';

/**
 * Page object for the jobs page
 * Handles job listings and applications
 */
export class JobsPage extends BasePage {
  // Selectors
  readonly jobsHeading = '[data-testid="vacancies-heading"]';
  readonly searchInput = 'input[placeholder*="search"], input.search-input';
  readonly searchButton = 'button.search-button';
  readonly jobCards = '[data-testid="vacancy-card"]';
  readonly applyButton = '[data-testid="apply-button"]';
  readonly coverLetterInput = 'textarea[placeholder*="cover letter"]';
  readonly submitApplicationButton = 'button[type="submit"]';
  readonly successMessage = '.success-message, .snackbar-message';
  readonly myApplicationsLink = 'a[href*="applications"]';
  readonly applicationItems = '.application-item';
  readonly withdrawButton = 'button.withdraw-button, button.cancel-button';
  readonly confirmButton = 'button.confirm-button';

  /**
   * @param page - Playwright page object
   */
  constructor(page: Page) {
    super(page);
  }

  /**
   * Navigate to jobs page
   */
  async goto(): Promise<void> {
    await this.navigate('/jobs');
  }

  /**
   * Search for jobs
   * @param keyword - Search keyword
   */
  async searchJobs(keyword: string): Promise<void> {
    await this.fill(this.searchInput, keyword);
    await this.click(this.searchButton);
    await this.waitForNavigation();
  }

  /**
   * Get number of job listings
   * @returns Number of job listings
   */
  async getJobCount(): Promise<number> {
    return await this.page.locator(this.jobCards).count();
  }

  /**
   * Click on a job card by index
   * @param index - Index of job card (0-based)
   */
  async clickJobCard(index: number = 0): Promise<void> {
    await this.page.locator(this.jobCards).nth(index).click();
    await this.waitForNavigation();
  }

  /**
   * Apply for a job
   * @param coverLetter - Cover letter text
   */
  async applyForJob(coverLetter: string): Promise<void> {
    await this.click(this.applyButton);

    // Check if cover letter input is visible
    if (await this.isVisible(this.coverLetterInput)) {
      await this.fill(this.coverLetterInput, coverLetter);
    }

    await this.click(this.submitApplicationButton);
    await this.waitForNavigation();
  }

  /**
   * Check if success message is visible
   * @returns True if success message is visible
   */
  async isSuccessMessageVisible(): Promise<boolean> {
    return await this.isVisible(this.successMessage);
  }

  /**
   * Navigate to my applications page
   */
  async navigateToMyApplications(): Promise<void> {
    await this.click(this.myApplicationsLink);
    await this.waitForNavigation();
  }

  /**
   * Get number of applications
   * @returns Number of applications
   */
  async getApplicationCount(): Promise<number> {
    return await this.page.locator(this.applicationItems).count();
  }

  /**
   * Withdraw an application by index
   * @param index - Index of application (0-based)
   */
  async withdrawApplication(index: number = 0): Promise<void> {
    await this.page.locator(this.applicationItems).nth(index).locator(this.withdrawButton).click();

    // Check if confirm button is visible
    if (await this.isVisible(this.confirmButton)) {
      await this.click(this.confirmButton);
    }

    await this.waitForNavigation();
  }

  /**
   * Alternative selectors using data-testid attributes for more resilient tests
   */
  async searchJobsWithRoleSelectors(keyword: string): Promise<void> {
    await this.page.locator(this.searchInput).fill(keyword);
    await this.page.locator(this.searchButton).click();
    await this.waitForNavigation();
  }

  async applyForJobWithRoleSelectors(coverLetter: string): Promise<void> {
    await this.page.locator('[data-testid="apply-button"]').click();

    const coverLetterInput = this.page.locator(this.coverLetterInput);
    if (await coverLetterInput.isVisible()) {
      await coverLetterInput.fill(coverLetter);
    }

    await this.page.locator(this.submitApplicationButton).click();
    await this.waitForNavigation();
  }
}
