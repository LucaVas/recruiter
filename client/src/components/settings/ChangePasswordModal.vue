<template>
  <Success
    :visible="passwordChanged"
    :title="'Password Changed!'"
    :message="'Your password was changed successfully'"
    @close="passwordChanged = false"
  />
  <Dialog
    :visible="props.visible"
    :header="'Change Password'"
    @update:visible="$emit('close')"
    closeOnEscape
    modal
    class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
  >
    <form class="space-y-4" aria-label="ChangePasswordModal" @submit.prevent="change">
      <div class="space-y-4">
        <Password
          v-model="form.oldPassword.value"
          :feedback="false"
          placeholder="Old Password"
          class="flex flex-col"
          required
          :invalid="!form.oldPassword.isValid"
          toggleMask
        />
        <Password
          v-model="form.newPassword.value"
          :feedback="false"
          placeholder="New Password"
          class="flex flex-col"
          required
          :invalid="!form.newPassword.isValid"
          toggleMask
        />
      </div>
      <div class="mt-3 flex justify-between">
        <Button label="Cancel" size="small" severity="secondary" outlined @click="$emit('close')" />
        <Button type="submit" size="small" label="Change" :loading="loading" :disabled="loading" />
      </div>
    </form>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import Password from 'primevue/password';
import { ApiError } from '@/utils/types';
import { showError } from '@/utils/errorUtils';
import { useToast } from 'primevue/usetoast';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import Success from '@/components/Success.vue';
import { type ChangePasswordRequest } from '@/stores/auth/schema';
import { changePassword } from '@/stores/user/index';
import { isPasswordValid, errorMessages } from '@/utils/validation/userFormValidationUtils';

const props = defineProps<{
  visible: boolean;
}>();
const emits = defineEmits<{
  (e: 'close'): void;
}>();

const toast = useToast();

export type ChangePasswordRequestForm = {
  [K in keyof ChangePasswordRequest]: {
    value: ChangePasswordRequest[K];
    isValid: boolean;
  };
};
const form = ref<ChangePasswordRequestForm>({
  oldPassword: { value: '', isValid: true },
  newPassword: { value: '', isValid: true },
});
const loading = ref(false);
const passwordChanged = ref(false);

const isValidInput = () => {
  const oldPassword = form.value.oldPassword.value;
  const newPassword = form.value.newPassword.value;

  if (oldPassword === newPassword) {
    form.value = {
      oldPassword: { value: oldPassword, isValid: false },
      newPassword: { value: newPassword, isValid: false },
    };
    showError(toast, errorMessages.samePasswords.message);
    return false;
  }

  const isOldPasswordValid = isPasswordValid(oldPassword);
  const isNewPasswordValid = isPasswordValid(newPassword);
  if (!isOldPasswordValid || !isNewPasswordValid) {
    form.value = {
      oldPassword: { value: oldPassword, isValid: isOldPasswordValid },
      newPassword: { value: newPassword, isValid: isNewPasswordValid },
    };
    showError(toast, errorMessages.invalidPassword.message);
    return false;
  }
  return true;
};

const change = async () => {
  loading.value = true;

  if (!isValidInput()) {
    loading.value = false;
    return;
  }

  try {
    await changePassword({
      oldPassword: form.value.oldPassword.value,
      newPassword: form.value.newPassword.value,
    });
    emits('close');
    passwordChanged.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
};

watch(
  () => props.visible,
  (open) => {
    if (open) {
      form.value = {
        oldPassword: { value: '', isValid: true },
        newPassword: { value: '', isValid: true },
      };
    }
  }
);
</script>
