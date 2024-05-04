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
    <div class="mb-5 flex flex-col gap-2 pt-3">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-at"></i>
        </InputGroupAddon>
        <InputText
          type="email"
          id="email"
          v-model="userForm.email"
          minlength="3"
          maxlength="50"
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
          id="mobile"
          v-model="userForm.mobile"
          mask="(999) 999-9999"
          placeholder="Mobile Number"
          class="md:w-14rem w-full"
          required
          :unmask="true"
        />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-id-card"></i>
        </InputGroupAddon>
        <InputText placeholder="City" autocomplete="off" v-model="userForm.city" />
      </InputGroup>
    </div>

    <div class="flex justify-end gap-2">
      <Button label="Cancel" severity="secondary" @click="$emit('close')" />
      <Button label="Save" @click="$emit('save', userForm)" />
    </div>
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
import { ref } from 'vue';
import { type UserInfoUpdateRequest } from '@/stores/auth/schema';

const { user, visible } = defineProps<{
  user: User | undefined;
  visible: boolean;
}>();

defineEmits<{
  (e: 'save', userForm: UserInfoUpdateRequest): void;
  (e: 'close'): void;
}>();

const userForm = ref({
  email: user ? user.email : '',
  mobile: user ? user.mobile : '',
  city: user ? user.city : '',
});
</script>
