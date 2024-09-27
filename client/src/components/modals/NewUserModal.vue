<template>
  <Success
    :visible="userCreated"
    :title="'User created!'"
    :message="'User is created successfully! They will receive their password via email soon.'"
    @close="userCreated = false"
  />
  <Dialog
    :visible="props.visible"
    :header="'New User'"
    @update:visible="$emit('close')"
    closeOnEscape
    modal
    class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
  >
    <form class="space-y-4" aria-label="NewUserModal" @submit.prevent="create">
      <!-- name -->
      <InputText
        id="name"
        v-model="form.name"
        minlength="3"
        maxlength="50"
        required
        placeholder="Name"
        class="md:w-14rem w-full"
      />
      <!-- email -->
      <InputText
        type="email"
        id="email"
        v-model="form.email"
        minlength="3"
        maxlength="50"
        required
        placeholder="Email"
        class="md:w-14rem w-full"
      />
      <!-- Phone -->
      <InputMask
        id="Phone"
        v-model="form.phone"
        mask="(999) 999-9999"
        placeholder="Phone Number"
        class="md:w-14rem w-full"
        required
        :unmask="true"
      />
      <!-- city -->
      <InputText
        id="city"
        placeholder="City"
        v-model="form.city"
        minlength="3"
        maxlength="50"
        class="md:w-14rem w-full"
        required
      />
      <div class="flex w-full justify-between gap-2">
        <!-- country -->
        <Dropdown
          data-testid="countryDropdown"
          v-model="form.country"
          :options="countries"
          optionLabel="label"
          optionValue="value"
          placeholder="Your Country"
          class="w-full"
          :highlightOnSelect="false"
          checkmark
          required
        />
        <!-- role -->
        <Dropdown
          data-testid="roleDropdown"
          v-model="form.roleName"
          :options="roles"
          optionLabel="label"
          optionValue="value"
          class="w-full"
          placeholder="Your Role"
          :highlightOnSelect="false"
          checkmark
          required
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
import { type NewUserRequest } from '@/types/userTypes';
import { ApiError } from '@/utils/types';
import { showError } from '@/utils/errorUtils';
import { useToast } from 'primevue/usetoast';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import Success from '@/components/Success.vue';
import useUserStore from '@/stores/userStore';

const props = defineProps<{
  visible: boolean;
}>();
const emits = defineEmits<{
  (e: 'close'): void;
}>();

const toast = useToast();

const form = ref<NewUserRequest>({
  name: '',
  email: '',
  phone: '',
  city: '',
  country: '',
  roleName: 'RECRUITER',
});
const resetForm = () => {
  form.value = {
    name: '',
    email: '',
    phone: '',
    city: '',
    country: '',
    roleName: 'RECRUITER',
  };
};
const loading = ref(false);
const userCreated = ref(false);

const countries = ref([{ label: 'India', value: 'india' }]);
const roles = ref([
  { label: 'Recruiter', value: 'RECRUITER' },
  { label: 'Admin', value: 'ADMIN' },
]);

const userStore = useUserStore();
const create = async () => {
  loading.value = true;

  try {
    await userStore.createNewUser(form.value);
    emits('close');
    userCreated.value = true;
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
