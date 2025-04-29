import { test, expect } from '../fixtures/auth-fixture';

test.describe('Authentication', () => {
  test('should navigate to login page', async ({ loginPage }) => {
    // Navigate to login page
    await loginPage.goto();

    // Verify we're on the login page
    await expect(loginPage.page).toHaveURL(/.*login/);
    await expect(loginPage.page.getByRole('heading', { name: /login/i })).toBeVisible();
  });

  test('should display validation errors with empty login form', async ({ loginPage }) => {
    // Navigate to login page
    await loginPage.goto();

    // Try to submit empty form
    await loginPage.page.getByRole('button', { name: /sign in/i }).click();

    // Check for validation error messages
    await expect(loginPage.page.getByText(/username is required/i)).toBeVisible();
    await expect(loginPage.page.getByText(/password is required/i)).toBeVisible();
  });

  test('should login with valid credentials', async ({ page, loginPage }) => {
    // Navigate to login page
    await loginPage.goto();

    // Fill in login form with valid credentials
    await loginPage.loginWithRoleSelectors('testuser', 'password123');

    // Verify successful login (redirect to dashboard or profile page)
    await expect(page).toHaveURL(/.*dashboard|profile/);

    // Verify user is logged in (e.g., username visible in header)
    await expect(page.getByText(/testuser/i)).toBeVisible();
  });

  test('should show error with invalid credentials', async ({ loginPage }) => {
    // Navigate to login page
    await loginPage.goto();

    // Fill in login form with invalid credentials
    await loginPage.loginWithRoleSelectors('wronguser', 'wrongpassword');

    // Verify error message is displayed
    await expect(loginPage.page.getByText(/invalid username or password/i)).toBeVisible();
  });

  test('should navigate to registration page', async ({ loginPage }) => {
    // Navigate to login page first
    await loginPage.goto();

    // Click on register link
    await loginPage.navigateToRegister();

    // Verify we're on the registration page
    await expect(loginPage.page).toHaveURL(/.*register/);
    await expect(loginPage.page.getByRole('heading', { name: /register|sign up/i })).toBeVisible();
  });

  test('should register a new user', async ({ page, loginPage }) => {
    // Navigate to login page first
    await loginPage.goto();

    // Navigate to registration page
    await loginPage.navigateToRegister();

    // Generate unique username
    const username = 'newuser' + Date.now();

    // Fill in registration form
    await page.getByLabel(/username/i).fill(username);
    await page.getByLabel(/password/i).fill('password123');
    await page.getByLabel(/first name/i).fill('Test');
    await page.getByLabel(/last name/i).fill('User');

    // Submit the form
    await page.getByRole('button', { name: /register|sign up/i }).click();

    // Verify successful registration (redirect to login or dashboard)
    await expect(page).toHaveURL(/.*login|dashboard/);

    // Verify success message
    await expect(page.getByText(/registration successful|account created/i)).toBeVisible();
  });

  test('should logout successfully', async ({ page, loginPage }) => {
    // Login first
    await loginPage.goto();
    await loginPage.loginWithRoleSelectors('testuser', 'password123');

    // Wait for login to complete
    await expect(page).toHaveURL(/.*dashboard|profile/);

    // Find and click logout button/link
    await page.getByRole('button', { name: /logout|sign out/i }).click();

    // Verify logout was successful (redirect to home/login page)
    await expect(page).toHaveURL(/.*\/$|login/);

    // Verify login link is visible again
    await expect(page.getByRole('link', { name: /login/i })).toBeVisible();
  });

  // Test using authenticated fixture
  test('should access protected page when authenticated', async ({ page }) => {
    // Navigate to homepage
    await page.goto('/');

    // Click the login button to open the login modal
    console.log('[DEBUG_LOG] Clicking login button');
    await page.click('button:has-text("Вхід")');

    // Wait for the login modal to appear
    console.log('[DEBUG_LOG] Waiting for login modal');
    await page.waitForSelector('[data-testid="username-input"]', { timeout: 5000 });

    // Fill in login form with valid credentials
    console.log('[DEBUG_LOG] Filling login form');
    await page.locator('[data-testid="username-input"]').fill('testuser');
    await page.locator('[data-testid="password-input"]').fill('password123');
    await page.locator('[data-testid="login-button"]').click();

    // Wait for navigation to complete
    await page.waitForLoadState('networkidle');

    // Navigate to profile page
    console.log('[DEBUG_LOG] Navigating to profile page');
    await page.goto('/profile');

    // Verify we're on the profile page
    await expect(page).toHaveURL(/.*profile/);

    // Wait for the profile page to load (it might show "Loading..." initially)
    console.log('[DEBUG_LOG] Waiting for profile title');
    await page.waitForSelector('[data-testid="profile-title"]', { timeout: 10000 });

    // Verify the profile title is visible
    await expect(page.locator('[data-testid="profile-title"]')).toBeVisible();

    // Log the actual text for debugging
    const titleText = await page.locator('[data-testid="profile-title"]').textContent();
    console.log(`[DEBUG_LOG] Profile title text: ${titleText}`);
  });
});

// Setup authentication state for other tests
test('setup authentication state', async ({ page, browser }) => {
  // Skip this test in CI environment
  test.skip(!!process.env.CI, 'Only run locally to set up auth state');

  const context = await browser.newContext();
  const setupPage = await context.newPage();

  // Set up authentication states for different user roles
  await setupPage.goto('/');

  // Import the AuthHelper directly in this test
  const { AuthHelper } = require('../utils/auth-helper');
  await AuthHelper.setupUserRoles(setupPage, context);

  await context.close();
});
