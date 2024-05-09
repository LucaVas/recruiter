<script lang="ts" setup>
import { ref } from 'vue';
import PageForm from '@/components/PageForm.vue';
import Button from 'primevue/button';
import Password from 'primevue/password';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import type { NewPasswordRequest } from '@/stores/auth/schema';
import { resetPassword } from '@/stores/auth/index';
import { useRoute } from 'vue-router';

const route = useRoute();
const urlToken = route.params.token as string;
const toast = useToast();
const resettingPassword = ref(false);
const passwordReset = ref(false);

const form = ref<NewPasswordRequest>({
  password: '',
});

const submit = async (token: string, form: NewPasswordRequest) => {
  resettingPassword.value = true;
  try {
    await resetPassword(token, form);
    toast.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Password reset successful. You can now login.',
    });
    passwordReset.value = true;
  } catch (err) {
    if (err instanceof ApiError)
      toast.add({
        severity: 'error',
        summary: 'Error',
        detail: err.message ?? DEFAULT_SERVER_ERROR,
        life: 3000,
      });
    if (err instanceof Error)
      toast.add({ severity: 'error', summary: 'Error', detail: DEFAULT_SERVER_ERROR, life: 3000 });
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
            class="w-full mt-5"
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
