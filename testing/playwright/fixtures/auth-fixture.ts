import { test as base, Page } from '@playwright/test';
import { AuthHelper } from '../utils/auth-helper';
import { LoginPage } from '../pages/LoginPage';
import { ProfilePage } from '../pages/ProfilePage';
import { JobsPage } from '../pages/JobsPage';

/**
 * Define the custom fixture types
 */
type PageObjects = {
  loginPage: LoginPage;
  profilePage: ProfilePage;
  jobsPage: JobsPage;
  authenticatedPage: Page;
  userPage: Page;
  adminPage: Page;
  recruiterPage: Page;
  authenticatedLoginPage: LoginPage;
  authenticatedProfilePage: ProfilePage;
  authenticatedJobsPage: JobsPage;
  userLoginPage: LoginPage;
  userProfilePage: ProfilePage;
  userJobsPage: JobsPage;
  adminLoginPage: LoginPage;
  adminProfilePage: ProfilePage;
  adminJobsPage: JobsPage;
  recruiterLoginPage: LoginPage;
  recruiterProfilePage: ProfilePage;
  recruiterJobsPage: JobsPage;
};

/**
 * Extend the default test fixture with page objects and authentication
 */
export const test = base.extend<PageObjects>({
  // Page objects
  loginPage: async ({ page }, use) => {
    await use(new LoginPage(page));
  },
  profilePage: async ({ page }, use) => {
    await use(new ProfilePage(page));
  },
  jobsPage: async ({ page }, use) => {
    await use(new JobsPage(page));
  },

  // Authentication fixtures
  authenticatedPage: async ({ browser }, use) => {
    // Create a new context with the saved storage state
    const context = await browser.newContext({
      storageState: AuthHelper.storageStateExists()
        ? AuthHelper.getStorageStatePath()
        : undefined
    });

    // Create a new page in this context
    const page = await context.newPage();

    // Use the authenticated page
    await use(page);

    // Close the context when done
    await context.close();
  },

  // User role fixtures
  userPage: async ({ browser }, use) => {
    const context = await browser.newContext({
      storageState: AuthHelper.storageStateExists('user-auth.json')
        ? AuthHelper.getStorageStatePath('user-auth.json')
        : undefined
    });
    const page = await context.newPage();
    await use(page);
    await context.close();
  },

  adminPage: async ({ browser }, use) => {
    const context = await browser.newContext({
      storageState: AuthHelper.storageStateExists('admin-auth.json')
        ? AuthHelper.getStorageStatePath('admin-auth.json')
        : undefined
    });
    const page = await context.newPage();
    await use(page);
    await context.close();
  },

  recruiterPage: async ({ browser }, use) => {
    const context = await browser.newContext({
      storageState: AuthHelper.storageStateExists('recruiter-auth.json')
        ? AuthHelper.getStorageStatePath('recruiter-auth.json')
        : undefined
    });
    const page = await context.newPage();
    await use(page);
    await context.close();
  },

  // Combined fixtures for page objects with authenticated pages
  authenticatedLoginPage: async ({ authenticatedPage }, use) => {
    await use(new LoginPage(authenticatedPage));
  },

  authenticatedProfilePage: async ({ authenticatedPage }, use) => {
    await use(new ProfilePage(authenticatedPage));
  },

  authenticatedJobsPage: async ({ authenticatedPage }, use) => {
    await use(new JobsPage(authenticatedPage));
  },

  // User role page objects
  userLoginPage: async ({ userPage }, use) => {
    await use(new LoginPage(userPage));
  },

  userProfilePage: async ({ userPage }, use) => {
    await use(new ProfilePage(userPage));
  },

  userJobsPage: async ({ userPage }, use) => {
    await use(new JobsPage(userPage));
  },

  // Admin role page objects
  adminLoginPage: async ({ adminPage }, use) => {
    await use(new LoginPage(adminPage));
  },

  adminProfilePage: async ({ adminPage }, use) => {
    await use(new ProfilePage(adminPage));
  },

  adminJobsPage: async ({ adminPage }, use) => {
    await use(new JobsPage(adminPage));
  },

  // Recruiter role page objects
  recruiterLoginPage: async ({ recruiterPage }, use) => {
    await use(new LoginPage(recruiterPage));
  },

  recruiterProfilePage: async ({ recruiterPage }, use) => {
    await use(new ProfilePage(recruiterPage));
  },

  recruiterJobsPage: async ({ recruiterPage }, use) => {
    await use(new JobsPage(recruiterPage));
  },
});

export { expect } from '@playwright/test';