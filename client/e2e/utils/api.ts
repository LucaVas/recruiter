import { fakeRecruiter } from './fakeData';
import type { Page } from '@playwright/test';
import userStore from '../../src/stores/user/index';

export async function signupNewUser(userSignup = fakeRecruiter()) {
  try {
    await userStore.signup(userSignup);
  } catch (error) {
    // ignore cases when user already exists
  }
}

export async function loginNewUser(page: Page, userLogin = fakeRecruiter()) {
  try {
    await userStore.signup(userLogin);
  } catch (error) {
    // ignore cases when user already exists
  }

  const res = await userStore.login({
    email: userLogin.email,
    password: userLogin.password,
  });
  const accessToken = res.data.token;
  await page.goto('/');

  await page.evaluate(
    ({ accessToken }) => {
      localStorage.setItem('token', accessToken);
    },
    { accessToken }
  );

  return userLogin;
}
