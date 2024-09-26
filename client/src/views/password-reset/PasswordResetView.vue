<script lang="ts" setup>
import PageForm from '@/components/PageForm.vue';
import Button from 'primevue/button';
import Password from 'primevue/password';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import type { NewPasswordRequest } from '@/types/authTypes';
import { resetPassword } from '@/api/userApi';
import { useRoute } from 'vue-router';
import { showError, showSuccess } from '@/utils/errorUtils';
import { resettingPassword, passwordReset, form } from './index';

const route = useRoute();
const urlToken = route.params.token as string;
const toast = useToast();

const submit = async (token: string, form: NewPasswordRequest) => {
  resettingPassword.value = true;
  try {
    await resetPassword(token, form);
    showSuccess(toast, 'Password reset successfully. You can now login.');
    passwordReset.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    resettingPassword.value = false;
  }
};
</script>

<template>
  <div class="flex h-screen w-screen justify-center bg-slate-100">
    <PageForm
      heading="Password Reset"
      formLabel="Password Reset"
      @submit="submit(urlToken, form)"
      data-testid="forgot-password-form"
    >
      <template #default>
        <!-- email -->
        <Password
          v-model="form.password"
          :feedback="false"
          placeholder="Password"
          minlength="8"
          maxlength="64"
          class="flex flex-col"
          autocomplete="current-password"
          required
        />
        <Button
          v-if="!passwordReset"
          class="w-full"
          type="submit"
          label="Create new password"
          :disabled="resettingPassword"
          :loading="resettingPassword"
          data-testid="password-reset-button"
        />
        <RouterLink :to="{ name: 'Login' }" v-else>
          <Button
            class="mt-5 w-full"
            type="button"
            label="Back to Login"
            data-testid="back-to-login-button"
          />
        </RouterLink>
      </template>

      <template #footer>
        <div class="mt-4">
          <RouterLink :to="{ name: 'Login' }" v-if="!passwordReset">
            <Button
              class="w-full"
              type="button"
              label="Don't want to reset? "
              icon="pi pi-question-circle"
              severity="secondary"
              :disabled="resettingPassword"
              data-testid="no-reset-button"
              outlined
            />
          </RouterLink>
        </div>
      </template>
    </PageForm>
  </div>
</template>
