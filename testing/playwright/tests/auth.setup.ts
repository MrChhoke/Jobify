import { test as setup } from '@playwright/test';
import { AuthHelper } from '../utils/auth-helper';

/**
 * This setup file is used to create authentication state for different user roles.
 * It runs before the actual tests to ensure that the authentication state is available.
 */

// Setup authentication for a regular user
setup('authenticate as user', async ({ page, context }) => {
  // Navigate to the application
  await page.goto('/');
  
  // Login and save authentication state for a regular user
  await AuthHelper.loginAndSaveState(
    page,
    context,
    'testuser',
    'password123',
    'user-auth.json'
  );
});

// Setup authentication for an admin user
setup('authenticate as admin', async ({ page, context }) => {
  // Navigate to the application
  await page.goto('/');
  
  // Login and save authentication state for an admin user
  await AuthHelper.loginAndSaveState(
    page,
    context,
    'admin',
    'admin123',
    'admin-auth.json'
  );
});

// Setup authentication for a recruiter user
setup('authenticate as recruiter', async ({ page, context }) => {
  // Navigate to the application
  await page.goto('/');
  
  // Login and save authentication state for a recruiter user
  await AuthHelper.loginAndSaveState(
    page,
    context,
    'recruiter',
    'recruiter123',
    'recruiter-auth.json'
  );
});

// Setup default authentication state
setup('authenticate default', async ({ page, context }) => {
  // Navigate to the application
  await page.goto('/');
  
  // Login and save authentication state for default use
  await AuthHelper.loginAndSaveState(
    page,
    context,
    'testuser',
    'password123'
  );
});