import { test, expect } from '@playwright/test';

test('basic navigation test', async ({ page }) => {
  // Navigate to the homepage
  await page.goto('/');
  
  // Add a debug log to see what's on the page
  const pageTitle = await page.title();
  console.log(`[DEBUG_LOG] Page title: ${pageTitle}`);
  
  // Take a screenshot for debugging
  await page.screenshot({ path: 'homepage.png' });
  
  // Log the HTML content of the page
  const content = await page.content();
  console.log(`[DEBUG_LOG] Page content length: ${content.length}`);
  
  // Check if the page loaded
  await expect(page).toHaveURL('/');
});