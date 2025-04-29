# Playwright E2E Tests for Jobify

This directory contains end-to-end tests for the Jobify application using Playwright, following the Page Object Model (POM) pattern and best practices.

## Project Structure

```
testing/playwright/
├── fixtures/           # Test fixtures and authentication state
│   └── auth-fixture.ts # Authentication fixtures for tests
├── pages/              # Page Object Model classes
│   ├── BasePage.ts     # Base page class with common methods
│   ├── LoginPage.ts    # Login page object
│   ├── ProfilePage.ts  # Profile page object
│   └── JobsPage.ts     # Jobs page object
├── tests/              # Test files organized by feature
│   ├── auth.spec.ts    # Authentication tests
│   ├── profile.spec.ts # Profile management tests
│   └── jobs.spec.ts    # Job listings and applications tests
├── utils/              # Utility functions and helpers
│   └── auth-helper.ts  # Authentication helper functions
├── playwright.config.ts # Playwright configuration
└── README.md           # Documentation
```

## Overview

These tests verify the functionality of the application from a user's perspective, simulating real user interactions with the UI. The tests cover:

- Authentication (login, registration, logout)
- Profile management (viewing and updating profile information)
- Job listings and applications (browsing, filtering, applying for jobs)

## Page Object Model (POM)

This project uses the Page Object Model pattern, which:

- Separates test logic from page interactions
- Improves test maintainability and readability
- Reduces code duplication
- Makes tests more resilient to UI changes

Each page in the application has a corresponding page object class that encapsulates the page's elements and actions.

## Authentication and Storage State

Tests use Playwright's storage state feature to:

- Save and reuse authentication state between tests
- Avoid repetitive login steps
- Test with different user roles (regular user, admin, recruiter)

The `auth-helper.ts` utility provides functions for managing authentication state, and the `auth-fixture.ts` file extends Playwright's test fixtures with authenticated page objects.

## Running Tests

You can run the tests using the following npm scripts:

```bash
# Run all tests
npm run test:e2e

# Run tests with UI mode (for debugging)
npm run test:e2e:ui

# Run tests in debug mode
npm run test:e2e:debug

# View the HTML report after running tests
npm run test:e2e:report

# Set up authentication state (run this first)
npx playwright test -g "setup authentication state"
```

## Configuration

The Playwright configuration is in `playwright.config.ts`. It includes:

- Multiple browser configurations (Chrome, Firefox, Safari, mobile browsers)
- Screenshot and trace settings for debugging
- Web server configuration to start the React app during testing

## Best Practices

### General Test Practices

1. **Isolation**: Each test should be independent and not rely on the state from other tests.
2. **Readability**: Use descriptive test names and comments to explain the purpose of each test.
3. **Robustness**: Use locators that are resilient to UI changes (e.g., roles, test IDs).
4. **Assertions**: Include meaningful assertions that verify the expected behavior.
5. **Error Handling**: Handle conditional UI elements and empty states appropriately.

### Page Object Model Best Practices

1. **Encapsulation**: Page objects should encapsulate all interactions with a page.
2. **Single Responsibility**: Each page object should represent a single page or component.
3. **No Assertions**: Page objects should not contain assertions; tests should handle assertions.
4. **Return Values**: Methods that navigate to a new page should return the new page object.
5. **Selectors**: Keep selectors in the page object, not in the test.

### Selectors Best Practices

Playwright recommends using the following selectors in order of preference:

1. **User-facing attributes**: `getByRole()`, `getByText()`, `getByLabel()`
2. **Test-specific attributes**: `data-testid` attributes with `getByTestId()`
3. **CSS selectors**: Only when the above methods aren't suitable

Example:
```typescript
// Good - uses user-facing attributes
await page.getByRole('button', { name: 'Submit' }).click();

// Avoid - brittle CSS selector
await page.locator('.submit-btn').click();
```

## Maintenance

When the application UI changes:

1. Update the affected page objects to match the new UI
2. Run the tests to ensure they still pass
3. Update this documentation if the test structure or best practices change
