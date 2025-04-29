import { test, expect } from '../fixtures/auth-fixture';

test.describe('Profile Management', () => {
  // Use the authenticated user fixture for all tests in this group
  test.use({ storageState: 'user-auth.json' });

  test('should display user profile information', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Verify profile page elements are visible
    await expect(userProfilePage.page.getByRole('heading', { name: /profile/i })).toBeVisible();
    
    // Check if user information is displayed
    await expect(userProfilePage.page.getByText(/username/i)).toBeVisible();
    await expect(userProfilePage.page.getByText(/first name/i)).toBeVisible();
    await expect(userProfilePage.page.getByText(/last name/i)).toBeVisible();
  });

  test('should update profile information successfully', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Generate unique values for testing
    const timestamp = Date.now();
    const newFirstName = `Test${timestamp}`;
    const newLastName = `User${timestamp}`;
    
    // Update profile information
    await userProfilePage.updateProfileWithRoleSelectors(newFirstName, newLastName);
    
    // Verify success message
    await expect(userProfilePage.isSuccessMessageVisible()).resolves.toBe(true);
    
    // Verify updated information is displayed
    const profileInfo = await userProfilePage.getProfileInfo();
    expect(profileInfo.firstName).toContain(newFirstName);
    expect(profileInfo.lastName).toContain(newLastName);
  });

  test('should display validation errors with empty fields', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Click edit profile button
    await userProfilePage.clickEditProfile();
    
    // Clear required fields
    await userProfilePage.page.getByLabel(/first name/i).clear();
    await userProfilePage.page.getByLabel(/last name/i).clear();
    
    // Submit the form
    await userProfilePage.page.getByRole('button', { name: /save|update/i }).click();
    
    // Verify validation error messages
    await expect(userProfilePage.page.getByText(/first name is required/i)).toBeVisible();
    await expect(userProfilePage.page.getByText(/last name is required/i)).toBeVisible();
  });

  test('should cancel profile update', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Get current profile information
    const initialProfileInfo = await userProfilePage.getProfileInfo();
    
    // Click edit profile button
    await userProfilePage.clickEditProfile();
    
    // Change profile information
    await userProfilePage.page.getByLabel(/first name/i).clear();
    await userProfilePage.page.getByLabel(/first name/i).fill('Temporary');
    await userProfilePage.page.getByLabel(/last name/i).clear();
    await userProfilePage.page.getByLabel(/last name/i).fill('Change');
    
    // Cancel the update
    await userProfilePage.cancelProfileUpdate();
    
    // Verify original information is still displayed
    const currentProfileInfo = await userProfilePage.getProfileInfo();
    expect(currentProfileInfo.firstName).toEqual(initialProfileInfo.firstName);
    expect(currentProfileInfo.lastName).toEqual(initialProfileInfo.lastName);
  });

  test('should navigate to change password page', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Navigate to change password page
    await userProfilePage.navigateToChangePassword();
    
    // Verify we're on the change password page
    await expect(userProfilePage.page).toHaveURL(/.*change-password/);
    await expect(userProfilePage.page.getByRole('heading', { name: /change password/i })).toBeVisible();
  });

  test('should change password successfully', async ({ userProfilePage }) => {
    // Navigate to profile page
    await userProfilePage.goto();
    
    // Navigate to change password page
    await userProfilePage.navigateToChangePassword();
    
    // Fill in password change form
    await userProfilePage.page.getByLabel(/current password/i).fill('password123');
    await userProfilePage.page.getByLabel(/new password/i).fill('newpassword123');
    await userProfilePage.page.getByLabel(/confirm password/i).fill('newpassword123');
    
    // Submit the form
    await userProfilePage.page.getByRole('button', { name: /update password/i }).click();
    
    // Verify success message
    await expect(userProfilePage.page.getByText(/password updated successfully/i)).toBeVisible();
  });

  // Test with different user roles
  test.describe('Admin profile', () => {
    test.use({ storageState: 'admin-auth.json' });
    
    test('should display admin-specific options', async ({ adminProfilePage }) => {
      // Navigate to profile page
      await adminProfilePage.goto();
      
      // Verify admin-specific elements are visible
      await expect(adminProfilePage.page.getByText(/admin dashboard|admin panel/i)).toBeVisible();
      await expect(adminProfilePage.page.getByRole('link', { name: /manage users/i })).toBeVisible();
    });
  });

  test.describe('Recruiter profile', () => {
    test.use({ storageState: 'recruiter-auth.json' });
    
    test('should display recruiter-specific options', async ({ recruiterProfilePage }) => {
      // Navigate to profile page
      await recruiterProfilePage.goto();
      
      // Verify recruiter-specific elements are visible
      await expect(recruiterProfilePage.page.getByText(/recruiter dashboard|recruiter panel/i)).toBeVisible();
      await expect(recruiterProfilePage.page.getByRole('link', { name: /post job|create vacancy/i })).toBeVisible();
    });
  });
});