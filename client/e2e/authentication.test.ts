import { test, expect } from '@playwright/test';
import { fakeRecruiter } from 'utils/fakeData';

/**
 * Created on: 2024-03-25
 * Related issues:
 */

const { email, username, password, mobile, city, country, role } = fakeRecruiter();

test.describe.serial('signup and login sequence', () => {
  test('user can signup', async ({ page }) => {
    await page.goto('/signup');
    const successMessage = page.getByTestId('success-message');
    await expect(successMessage).toBeHidden();

    const form = page.getByRole('form', { name: 'Signup' });
    await form.locator('input[id="username"]').fill(username);
    await form.locator('input[id="email"]').fill(email);
    await form.getByPlaceholder('Password').fill(password);
    await form.locator('input[id="phone"]').fill(mobile);
    await form.locator('input[id="city"]').fill(city);
    const countryInput = page.getByPlaceholder('Country');
    await countryInput.fill(country);
    await countryInput.press('Enter');
    const roleInput = page.getByPlaceholder('Role');
    await roleInput.fill(role);
    await roleInput.press('Enter');
    await form.locator('button[type="submit"]').click();

    await expect(successMessage).toBeVisible({ timeout: 5000 });
  });

  test('user can not access his dashboard before login', async ({ page }) => {
    await page.goto('/');
    await page.waitForURL('/login');
  });

  test('visitor can login', async ({ page }) => {
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

  test('user can log out', async ({ page }) => {
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