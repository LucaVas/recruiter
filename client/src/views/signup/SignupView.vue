<script lang="ts" setup>
import { ref } from 'vue';
import PageForm from '@/components/PageForm.vue';
import InputText from 'primevue/inputtext';
import InputMask from 'primevue/inputmask';
import Dropdown from 'primevue/dropdown';
import Password from 'primevue/password';
import { useToast } from 'primevue/usetoast';
import { ApiError } from '../../utils/types';
import Toast from 'primevue/toast';
import type { RoleName } from '@/stores/user/schema';
import SignupCommentsModal from '@/components/signup/SignupCommentsModal.vue';
import { signup } from '@/stores/auth/index';
import type { SignupRequest } from '@/stores/auth/schema';
import { useRouter } from 'vue-router';

const router = useRouter();
const toast = useToast();
const showError = (content: string) => {
  toast.add({ severity: 'error', summary: 'Error', detail: content, life: 3000 });
};
const showSuccess = (content: string) => {
  toast.add({
    severity: 'success',
    summary: 'Success',
    detail: content,
    closable: true,
  });
};
const signupCommentsModalOpen = ref(false);
const countries = ref([{ label: 'India', value: 'india' }]);
const roles = ref([
  { label: 'Recruiter', value: 'ROLE_RECRUITER' },
  { label: 'Admin', value: 'ROLE_ADMIN' },
]);
const userForm = ref<SignupRequest>({
  username: '',
  email: '',
  password: '',
  mobile: '',
  city: '',
  country: '',
  roleName: '' as RoleName,
  comments: '',
});

const hasSucceeded = ref(false);
const loading = ref(false);

const submitSignup = async () => {
  loading.value = true;
  try {
    await signup(userForm.value);
    hasSucceeded.value = true;
    setTimeout(() => {
      showSuccess(
        'You have successfully signed up! An administrator will review your registration and confirm or reject it.'
      );
    }, 100);
    router.push({ name: 'Login' });
  } catch (err) {
    if (err instanceof ApiError) showError(err.message);
    if (err instanceof Error) showError('Something went wrong');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="flex h-full w-full items-center justify-center bg-slate-100">
    <Toast data-testid="message-toast" />
    <PageForm
      heading="Sign up"
      name="Signup"
      formLabel="Signup"
      @submit="signupCommentsModalOpen = true"
      data-testid="signup-form"
    >
      <template #default>
        <!-- username -->
        <InputText
          id="username"
          v-model="userForm.username"
          minlength="3"
          maxlength="50"
          required
          placeholder="Username"
          class="md:w-14rem w-full"
        />
        <!-- email -->
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
        <!-- password -->
        <Password
          v-model="userForm.password"
          class="md:w-14rem flex flex-col"
          placeholder="Password"
          id="password"
          minlength="8"
          maxlength="64"
          :feedback="false"
          required
        >
        </Password>
        <!-- mobile -->
        <InputMask
          id="mobile"
          v-model="userForm.mobile"
          mask="(999) 999-9999"
          placeholder="Mobile Number"
          class="md:w-14rem w-full"
          required
          :unmask="true"
        />
        <!-- city -->
        <InputText
          id="city"
          placeholder="City"
          v-model="userForm.city"
          minlength="3"
          maxlength="50"
          class="md:w-14rem w-full"
          required
        />
        <div class="flex w-full justify-between gap-2">
          <!-- country -->
          <Dropdown
            data-testid="countryDropdown"
            v-model="userForm.country"
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
            v-model="userForm.roleName"
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

        <!-- comments -->
        <SignupCommentsModal
          :visible="signupCommentsModalOpen"
          @closeModal="signupCommentsModalOpen = false"
          @continueSignup="
            (comments) => {
              userForm.comments = comments;
              signupCommentsModalOpen = false;
              submitSignup();
            }
          "
        />
        <div class="grid gap-2">
          <Button
            type="submit"
            color="red"
            label="Sign up"
            :loading="loading"
            :disabled="loading"
          />
        </div>
      </template>
      <template #footer>
        <div>
          <Message
            class="text-center"
            severity="contrast"
            :sticky="true"
            outlined
            :closable="false"
            data-testid="alredy-member-message"
          >
            Already a member?
            {{ ' ' }}
            <RouterLink :to="{ name: 'Login' }" class="font-semibold leading-6">Log in</RouterLink>
          </Message>
        </div>
      </template>
    </PageForm>
  </div>
</template>
