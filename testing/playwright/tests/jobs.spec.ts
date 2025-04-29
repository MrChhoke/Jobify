import { test, expect } from '../fixtures/auth-fixture';

test.describe('Job Listings and Applications', () => {
  // Use the authenticated user fixture for all tests in this group
  test.use({ storageState: 'user-auth.json' });

  test('should display job listings', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Verify job listings page elements are visible
    await expect(userJobsPage.page.getByRole('heading', { name: /jobs|vacancies/i })).toBeVisible();
    
    // Check if job listings are displayed
    const jobCount = await userJobsPage.getJobCount();
    expect(jobCount).toBeGreaterThan(0);
    
    // Verify job listing contains expected elements
    const firstJobCard = userJobsPage.page.locator(userJobsPage.jobCards).first();
    await expect(firstJobCard.getByText(/title|position/i)).toBeVisible();
    await expect(firstJobCard.getByText(/company|employer/i)).toBeVisible();
    await expect(firstJobCard.getByText(/location/i)).toBeVisible();
  });

  test('should filter job listings', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Get initial job count
    const initialJobCount = await userJobsPage.getJobCount();
    
    // Search for jobs
    await userJobsPage.searchJobsWithRoleSelectors('developer');
    
    // Get filtered job count
    const filteredJobCount = await userJobsPage.getJobCount();
    
    // Either we have fewer results or we verify the content contains our search term
    if (filteredJobCount < initialJobCount) {
      expect(filteredJobCount).toBeLessThan(initialJobCount);
    } else {
      // If count didn't change, verify content contains search term
      const jobsWithSearchTerm = await userJobsPage.page.locator(userJobsPage.jobCards)
        .filter({ hasText: /developer/i })
        .count();
      expect(jobsWithSearchTerm).toBeGreaterThan(0);
    }
  });

  test('should view job details', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Click on the first job listing
    await userJobsPage.clickJobCard(0);
    
    // Verify job details page is displayed
    await expect(userJobsPage.page).toHaveURL(/.*jobs|vacancies\/\d+/);
    await expect(userJobsPage.page.getByRole('heading', { name: /job details|vacancy details/i })).toBeVisible();
    
    // Check if detailed information is displayed
    await expect(userJobsPage.page.getByText(/description/i)).toBeVisible();
    await expect(userJobsPage.page.getByText(/requirements/i)).toBeVisible();
    await expect(userJobsPage.page.getByRole('button', { name: /apply|apply now/i })).toBeVisible();
  });

  test('should apply for a job', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Click on the first job listing
    await userJobsPage.clickJobCard(0);
    
    // Apply for the job
    await userJobsPage.applyForJobWithRoleSelectors('I am very interested in this position and believe my skills match your requirements.');
    
    // Verify success message
    await expect(userJobsPage.isSuccessMessageVisible()).resolves.toBe(true);
  });

  test('should view my applications', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Navigate to my applications page
    await userJobsPage.navigateToMyApplications();
    
    // Verify my applications page is displayed
    await expect(userJobsPage.page).toHaveURL(/.*applications|my-applications/);
    await expect(userJobsPage.page.getByRole('heading', { name: /my applications/i })).toBeVisible();
    
    // Check if applications are listed or empty state is shown
    const applicationCount = await userJobsPage.getApplicationCount();
    
    if (applicationCount > 0) {
      // If there are applications, verify their content
      const firstApplication = userJobsPage.page.locator(userJobsPage.applicationItems).first();
      await expect(firstApplication.getByText(/status/i)).toBeVisible();
      await expect(firstApplication.getByText(/date applied/i)).toBeVisible();
      await expect(firstApplication.getByText(/job title|position/i)).toBeVisible();
    } else {
      // If no applications, verify empty state message
      await expect(userJobsPage.page.getByText(/no applications|you haven't applied/i)).toBeVisible();
    }
  });

  test('should withdraw an application', async ({ userJobsPage }) => {
    // Navigate to jobs page
    await userJobsPage.goto();
    
    // Navigate to my applications page
    await userJobsPage.navigateToMyApplications();
    
    // Check if there are any applications
    const applicationCount = await userJobsPage.getApplicationCount();
    
    if (applicationCount > 0) {
      // Withdraw the first application
      await userJobsPage.withdrawApplication(0);
      
      // Verify success message
      await expect(userJobsPage.page.getByText(/application withdrawn|cancelled successfully/i)).toBeVisible();
      
      // Verify application count decreased
      const newApplicationCount = await userJobsPage.getApplicationCount();
      expect(newApplicationCount).toBeLessThan(applicationCount);
    } else {
      // Skip test if no applications
      test.skip();
    }
  });

  // Test with different user roles
  test.describe('Recruiter job management', () => {
    test.use({ storageState: 'recruiter-auth.json' });
    
    test('should be able to post a new job', async ({ recruiterJobsPage }) => {
      // Navigate to recruiter dashboard
      await recruiterJobsPage.page.goto('/recruiter-dashboard');
      
      // Click on post job button
      await recruiterJobsPage.page.getByRole('link', { name: /post job|create vacancy/i }).click();
      
      // Fill in job details
      await recruiterJobsPage.page.getByLabel(/title|position/i).fill('Senior Developer');
      await recruiterJobsPage.page.getByLabel(/company|employer/i).fill('Tech Company');
      await recruiterJobsPage.page.getByLabel(/location/i).fill('Remote');
      await recruiterJobsPage.page.getByLabel(/description/i).fill('We are looking for a senior developer to join our team.');
      await recruiterJobsPage.page.getByLabel(/requirements/i).fill('5+ years of experience, strong problem-solving skills');
      await recruiterJobsPage.page.getByLabel(/salary/i).fill('100000');
      
      // Submit the form
      await recruiterJobsPage.page.getByRole('button', { name: /submit|post/i }).click();
      
      // Verify success message
      await expect(recruiterJobsPage.page.getByText(/job posted successfully/i)).toBeVisible();
    });
  });
});