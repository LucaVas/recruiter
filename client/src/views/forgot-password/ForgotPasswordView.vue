<script lang="ts" setup>
import { ref } from 'vue';
import PageForm from '@/components/PageForm.vue';
import Toast from 'primevue/toast';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '@/utils/types';
import { type PasswordForgotRequest } from '@/stores/auth/schema';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();
const loading = ref(false);

const form = ref<PasswordForgotRequest>({
  email: '',
});


const submit = async (form: PasswordForgotRequest) => {
  loading.value = true;
  try {
    //await requestNewPassword(form);
    toast.add({ severity: 'success', summary: 'Success', detail: 'A password reset link was sent to your email.' });
  } catch (err) {
    if (err instanceof ApiError)
      toast.add({ severity: 'error', summary: 'Error', detail: err.message, life: 3000 });
    if (err instanceof Error)
      toast.add({ severity: 'error', summary: 'Error', detail: DEFAULT_SERVER_ERROR, life: 3000 });
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <Toast data-testid="error-message" />

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
      </template>

      <template #footer>
        <div class="mt-4 flex">
          <Button
            class="w-full"
            type="submit"
            label="Reset password"
            :disabled="loading"
            data-testid="password-reset-button"
          />
        </div>
      </template>
    </PageForm>
  </div>
</template>
