<template>
  <Dialog
    v-if="userForm"
    :visible="visible"
    @update:visible="$emit('close')"
    closeOnEscape
    modal
    header="Edit Information"
    class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
  >
    <form class="mb-5 flex flex-col gap-2 pt-3" @submit.prevent="submit">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-at"></i>
        </InputGroupAddon>
        <InputText
          type="email"
          id="email"
          v-model="userForm.email.value"
          :invalid="!userForm.email.isValid"
          required
          placeholder="Email"
          class="md:w-14rem w-full"
        />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-phone"></i>
        </InputGroupAddon>
        <InputMask
          id="phone"
          v-model="userForm.phone.value"
          :invalid="!userForm.phone.isValid"
          mask="(999) 999-9999"
          placeholder="Mobile Phone"
          class="md:w-14rem w-full"
          required
          :unmask="true"
        />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-id-card"></i>
        </InputGroupAddon>
        <InputText
          placeholder="City"
          autocomplete="off"
          v-model="userForm.city.value"
          :invalid="!userForm.city.isValid"
          required
        />
      </InputGroup>
      <div class="mt-3 flex justify-between">
        <Button label="Cancel" size="small" severity="secondary" outlined @click="$emit('close')" />
        <Button label="Save" size="small" type="submit" />
      </div>
    </form>
  </Dialog>
</template>

<script setup lang="ts">
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import InputMask from 'primevue/inputmask';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import { type User } from '@/stores/user/schema';
import { ref, computed, watch } from 'vue';
import { type UserInfoUpdateRequest } from '@/stores/auth/schema';
import { showError } from '@/utils/errorUtils';
import { useToast } from 'primevue/usetoast';
import {
  errorMessages,
  isPhoneValid,
  isCityValid,
  isEmailValid,
} from '@/utils/validation/userFormValidationUtils';

const props = defineProps<{
  user: User | undefined;
  visible: boolean;
}>();

const emits = defineEmits<{
  (e: 'save', userForm: UserInfoUpdateRequest, loginRequired: boolean): void;
  (e: 'close'): void;
}>();

const submit = () => {
  if (!isValidInput()) {
    return;
  }
  const validatedForm = {
    email: userForm.value.email.value,
    phone: userForm.value.phone.value,
    city: userForm.value.city.value,
  };
  emits('save', validatedForm, loginRequired.value);
};

const toast = useToast();
const isValidInput = () => {
  const email = userForm.value.email.value;
  const phone = userForm.value.phone.value;
  const city = userForm.value.city.value;

  const isValidEmail = isEmailValid(email);
  const isValidPhone = isPhoneValid(phone);
  const isValidCity = isCityValid(city);

  if (!isValidEmail) {
    userForm.value = {
      ...userForm.value,
      email: { value: email, isValid: isValidEmail },
    };
    showError(toast, errorMessages.invalidEmail.message);
    return false;
  }

  if (!isValidPhone) {
    userForm.value = {
      ...userForm.value,
      phone: { value: phone, isValid: isValidPhone },
    };
    showError(toast, errorMessages.invalidPhone.message);
    return false;
  }

  if (!isValidCity) {
    userForm.value = {
      ...userForm.value,
      city: { value: city, isValid: isValidCity },
    };
    showError(toast, errorMessages.invalidCity.message);
    return false;
  }

  return true;
};

const loginRequired = computed(
  () => (props.user && props.user.email !== userForm.value.email.value) ?? false
);

const userForm = ref();
watch(
  () => props.visible,
  (open) => {
    if (open) {
      userForm.value = {
        email: { value: props.user ? props.user.email : '', isValid: true },
        phone: { value: props.user ? props.user.phone : '', isValid: true },
        city: { value: props.user ? props.user.city : '', isValid: true },
      };
    }
  }
);
</script>
