import { test, expect } from '@playwright/test';
import { fakeRecruiter } from 'utils/fakeData';

/**
 * Created on: 2024-04-14
 * Related issues:
 */

const { email, username, password, mobile, city } = fakeRecruiter();

test.describe.serial('signup and login sequence', () => {
  test('user can signup', async ({ page }) => {
    await page.goto('/signup');
    const messageToast = page.getByTestId('message-toast');
    await expect(messageToast).toBeHidden();

    const form = page.getByRole('form', { name: 'Signup' });
    await form.locator('input[id="username"]').fill(username);
    await form.locator('input[id="email"]').fill(email);
    await form.getByPlaceholder('Password').fill(password);
    await form.locator('input[id="mobile"]').fill(mobile);
    await form.locator('input[id="city"]').fill(city);

    const countryDropdown = page.getByTestId('countryDropdown');
    const country = page.getByText('India');
    await countryDropdown.click();
    await country.click();

    const roleDropdown = page.getByTestId('roleDropdown');
    const role = page.getByText('Recruiter');
    await roleDropdown.click();
    await role.click();

    await form.locator('button[type="submit"]').click();
    await page.locator('#confirmSignupButton').click();

    await expect(page).toHaveURL('/login');
  });

  test.skip('user can not access his dashboard before login', async ({ page }) => {
    await page.goto('/');
    await page.waitForURL('/login');
  });

  test.skip('visitor can login', async ({ page }) => {
    await page.goto('/login');

    const myJobsLink = page.getByText('My jobs');
    await expect(myJobsLink).toBeHidden();

    const form = page.getByRole('form', { name: 'Login' });
    await form.locator('input[type="email"]').fill(email);
    await form.locator('input[type="password"]').fill(password);
    await form.locator('button[type="submit"]').click();

    await expect(myJobsLink).toBeVisible({ timeout: 10000 });

    await page.reload();
    await expect(myJobsLink).toBeVisible();
  });

  test.skip('user can log out', async ({ page }) => {
    await page.goto('/login');

    const myJobsLink = page.getByText('My jobs');
    const logoutButton = page.getByText('Logout');
    await expect(myJobsLink).toBeHidden();
    await expect(logoutButton).toBeHidden();

    const form = page.getByRole('form', { name: 'Login' });
    await form.locator('input[type="email"]').fill(email);
    await form.locator('input[type="password"]').fill(password);
    await form.locator('button[type="submit"]').click();

    await expect(myJobsLink).toBeVisible();
    await expect(logoutButton).toBeVisible();
    await logoutButton.click();

    await expect(page).toHaveURL('/login');
    await expect(logoutButton).toBeHidden();
    await page.goto('/');
    await expect(logoutButton).toBeHidden();
    await expect(page).toHaveURL('/login');
  });
});

test.describe('login validation', () => {
  test.skip('User cannot login without registration', async ({ page }) => {
    const errorMessage = page.getByTestId('error-message');
    await page.goto('/login');

    const myJobsLink = page.getByText('My jobs');
    await expect(myJobsLink).toBeHidden();

    const form = page.getByRole('form', { name: 'Login' });
    await form.locator('input[type="email"]').fill('invalid@email.com');
    await form.locator('input[type="password"]').fill('invalidPassword123');
    await form.locator('button[type="submit"]').click();

    await expect(errorMessage).toBeVisible();
    await expect(errorMessage).toHaveText('Invalid credentials');
    await expect(myJobsLink).toBeHidden();
  });
});
