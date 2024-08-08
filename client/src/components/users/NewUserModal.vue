<template>
  <Dialog
    :visible="props.visible"
    :header="'New User'"
    @update:visible="$emit('close')"
    closeOnEscape
    modal
    class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
  >
    <form class="space-y-4" aria-label="NewUserModal" @submit.prevent="handleSubmit">
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
import { ref } from 'vue';
import { type NewUserRequest } from '@/stores/user/schema';

const props = defineProps<{
  visible: boolean;
}>();
defineEmits<{
  (e: 'close'): void;
}>();

const form = ref<NewUserRequest>({
  name: '',
  email: '',
  phone: '',
  city: '',
  country: '',
  roleName: 'RECRUITER',
});
const loading = ref(false);

const countries = ref([{ label: 'India', value: 'india' }]);
const roles = ref([
  { label: 'Recruiter', value: 'RECRUITER' },
  { label: 'Admin', value: 'ADMIN' },
]);

const handleSubmit = () => {
  loading.value = true;
  // Assume a request is made here
  setTimeout(() => {
    loading.value = false;
    // emit('close');
  }, 2000); // Simulating async operation
};
</script>
