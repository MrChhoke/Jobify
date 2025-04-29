import { Page } from '@playwright/test';
import { BasePage } from './BasePage';

/**
 * Page object for the profile page
 * Handles profile-related actions
 */
export class ProfilePage extends BasePage {
  // Selectors
  readonly profileHeading = '[data-testid="profile-title"]';
  readonly editProfileButton = '[data-testid="edit-button"]';
  readonly firstNameInput = '[data-testid="firstname-input"]';
  readonly lastNameInput = '[data-testid="lastname-input"]';
  readonly saveButton = '[data-testid="save-button"]';
  readonly cancelButton = 'button:has-text("Скасувати")';
  readonly successMessage = '[data-testid="success-snackbar"]';
  readonly changePasswordLink = 'a:has-text("Змінити пароль")';
  readonly addRecruiterButton = '[data-testid="add-recruiter-button"]';
  readonly recruiterFormModal = '[data-testid="recruiter-form-modal"]';
  readonly recruiterFirstNameInput = '[data-testid="recruiter-firstname-input"]';
  readonly recruiterLastNameInput = '[data-testid="recruiter-lastname-input"]';
  readonly recruiterCompanyInput = '[data-testid="recruiter-company-input"]';
  readonly recruiterUsernameInput = '[data-testid="recruiter-username-input"]';
  readonly recruiterPasswordInput = '[data-testid="recruiter-password-input"]';
  readonly createRecruiterButton = '[data-testid="create-recruiter-button"]';

  /**
   * @param page - Playwright page object
   */
  constructor(page: Page) {
    super(page);
  }

  /**
   * Navigate to profile page
   */
  async goto(): Promise<void> {
    await this.navigate('/profile');
  }

  /**
   * Click edit profile button
   */
  async clickEditProfile(): Promise<void> {
    await this.click(this.editProfileButton);
  }

  /**
   * Update profile information
   * @param firstName - First name
   * @param lastName - Last name
   */
  async updateProfile(firstName: string, lastName: string): Promise<void> {
    await this.clickEditProfile();
    await this.fill(this.firstNameInput, firstName);
    await this.fill(this.lastNameInput, lastName);
    await this.click(this.saveButton);
    await this.waitForNavigation();
  }

  /**
   * Cancel profile update
   */
  async cancelProfileUpdate(): Promise<void> {
    await this.click(this.cancelButton);
  }

  /**
   * Get success message text
   * @returns Success message text
   */
  async getSuccessMessage(): Promise<string> {
    return await this.getText(this.successMessage);
  }

  /**
   * Check if success message is visible
   * @returns True if success message is visible
   */
  async isSuccessMessageVisible(): Promise<boolean> {
    return await this.isVisible(this.successMessage);
  }

  /**
   * Navigate to change password page
   */
  async navigateToChangePassword(): Promise<void> {
    await this.click(this.changePasswordLink);
    await this.waitForNavigation();
  }

  /**
   * Get profile information
   * @returns Object containing profile information
   */
  async getProfileInfo(): Promise<{ firstName: string; lastName: string }> {
    const firstName = await this.page.locator('[data-testid="firstname-value"]').textContent() || '';
    const lastName = await this.page.locator('[data-testid="lastname-value"]').textContent() || '';

    return {
      firstName: firstName.trim(),
      lastName: lastName.trim()
    };
  }

  /**
   * Alternative selectors using data-testid attributes for more resilient tests
   */
  async updateProfileWithRoleSelectors(firstName: string, lastName: string): Promise<void> {
    await this.page.locator(this.editProfileButton).click();
    await this.page.locator(this.firstNameInput).clear();
    await this.page.locator(this.firstNameInput).fill(firstName);
    await this.page.locator(this.lastNameInput).clear();
    await this.page.locator(this.lastNameInput).fill(lastName);
    await this.page.locator(this.saveButton).click();
    await this.waitForNavigation();
  }

  /**
   * Create a new recruiter (admin only)
   * @param firstName - Recruiter's first name
   * @param lastName - Recruiter's last name
   * @param company - Recruiter's company
   * @param username - Recruiter's username
   * @param password - Recruiter's password
   */
  async createRecruiter(
    firstName: string,
    lastName: string,
    company: string,
    username: string,
    password: string
  ): Promise<void> {
    // Click the add recruiter button
    await this.page.locator(this.addRecruiterButton).click();

    // Wait for the modal to appear
    await this.page.locator(this.recruiterFormModal).waitFor({ state: 'visible' });

    // Fill in the form
    await this.page.locator(this.recruiterFirstNameInput).fill(firstName);
    await this.page.locator(this.recruiterLastNameInput).fill(lastName);
    await this.page.locator(this.recruiterCompanyInput).fill(company);
    await this.page.locator(this.recruiterUsernameInput).fill(username);
    await this.page.locator(this.recruiterPasswordInput).fill(password);

    // Submit the form
    await this.page.locator(this.createRecruiterButton).click();

    // Wait for the success message
    await this.page.locator(this.successMessage).waitFor({ state: 'visible' });
  }
}
