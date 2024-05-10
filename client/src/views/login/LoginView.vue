<script lang="ts" setup>
import PageForm from '@/components/PageForm.vue';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import { userForm, loading } from './index';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { login } from '@/stores/auth';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import { useRouter } from 'vue-router';

const toast = useToast();
const router = useRouter();

const submitLogin = async () => {
  loading.value = true;
  try {
    await login(userForm.value);
    router.push({ name: 'Dashboard' });
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="flex h-screen w-screen justify-center bg-slate-100">
    <PageForm heading="Log in" formLabel="Login" @submit="submitLogin" data-testid="login-form">
      <template #default>
        <!-- email -->
        <InputText
          type="email"
          id="email "
          v-model="userForm.usernameOrEmail"
          minlength="3"
          maxlength="50"
          required
          autocomplete="email"
          aria-describedby="username-help"
          placeholder="Email"
          class="w-full"
        />

        <Password
          v-model="userForm.password"
          :feedback="false"
          placeholder="Password"
          minlength="8"
          maxlength="64"
          class="flex flex-col"
          autocomplete="current-password"
          required
        />

        <div class="grid grid-flow-row auto-rows-max gap-2">
          <Button
            type="submit"
            label="Login"
            :loading="loading"
            :disabled="loading"
            class="flex items-center justify-center"
          />
        </div>
      </template>

      <template #footer>
        <div class="mt-4">
          <RouterLink :to="{ name: 'ForgotPassword' }">
            <Button
              class="mb-4 w-full"
              type="button"
              label="Forgot password?"
              :disabled="loading"
              data-testid="reset-password-button"
              outlined
            />
          </RouterLink>
          <RouterLink :to="{ name: 'Signup' }">
            <Button
              class="w-full"
              type="button"
              label="Not a member?"
              icon="pi pi-question-circle"
              severity="secondary"
              :disabled="loading"
              data-testid="not-member-button"
              outlined
            />
          </RouterLink>
        </div>
      </template>
    </PageForm>
  </div>
</template>
