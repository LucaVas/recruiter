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
          v-model="form.oldPassword"
          :feedback="false"
          placeholder="Password"
          minlength="8"
          maxlength="64"
          class="flex flex-col"
          required
          toggleMask
        />
        <Password
          v-model="form.newPassword"
          :feedback="false"
          placeholder="Password"
          minlength="8"
          maxlength="64"
          class="flex flex-col"
          required
          toggleMask
        />
      </div>
      <div class="grid gap-2">
        <Button
          type="submit"
          color="red"
          label="Create User"
          :loading="loading"
          :disabled="loading"
        />
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
import { changePassword } from '../../stores/user/index';

const props = defineProps<{
  visible: boolean;
}>();
const emits = defineEmits<{
  (e: 'close'): void;
}>();

const toast = useToast();

const form = ref<ChangePasswordRequest>({
  oldPassword: '',
  newPassword: '',
});
const resetForm = () => {
  form.value = {
    oldPassword: '',
    newPassword: '',
  };
};
const loading = ref(false);
const passwordChanged = ref(false);

const change = async () => {
  loading.value = true;
  if (form.value.oldPassword != form.value.newPassword) {
    showError(toast, 'The two passwords entered do not match.');
    loading.value = false;
    return;
  }

  try {
    await changePassword(form.value);
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
      resetForm();
    }
  }
);
</script>
