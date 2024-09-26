<script lang="ts" setup>
import PageForm from '@/components/PageForm.vue';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { type PasswordForgotRequest } from '@/types/authTypes';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import { requestNewPassword } from '@/api/userApi';
import { showSuccess } from '@/utils/errorUtils';
import { showError } from '@/utils/errorUtils';
import { ref } from 'vue';

const toast = useToast();
const sendingEmail = ref(false);

const form = ref<PasswordForgotRequest>({
  email: '',
  name: '',
});

const submit = async (form: PasswordForgotRequest) => {
  sendingEmail.value = true;
  try {
    await requestNewPassword(form);
    showSuccess(toast, 'A password reset link was sent to your email.');
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    sendingEmail.value = false;
  }
};
</script>

<template>
  <div class="flex h-screen w-screen justify-center bg-slate-100">
    <PageForm
      heading="Password Reset"
      formLabel="Password Reset"
      @submit="submit(form)"
      data-testid="forgot-password-form"
    >
      <template #default>
        <!-- email -->
        <InputText
          type="email"
          id="email "
          v-model="form.email"
          minlength="3"
          maxlength="50"
          required
          autocomplete="email"
          placeholder="Email"
          class="w-full"
        />
        <InputText
          type="name"
          id="name "
          v-model="form.name"
          minlength="3"
          maxlength="50"
          required
          autocomplete="name"
          placeholder="Name"
          class="w-full"
        />
        <Button
          class="w-full"
          type="submit"
          label="Reset password"
          :disabled="sendingEmail"
          :loading="sendingEmail"
          data-testid="password-reset-button"
        />
      </template>

      <template #footer>
        <div class="mt-4">
          <RouterLink :to="{ name: 'Login' }">
            <Button
              class="w-full"
              type="button"
              label="Remember now?"
              icon="pi pi-question-circle"
              severity="secondary"
              :disabled="sendingEmail"
              data-testid="remember-password-button"
              outlined
            />
          </RouterLink>
        </div>
      </template>
    </PageForm>
  </div>
</template>
