<script lang="ts" setup>
import PageForm from '@/components/PageForm.vue';
import { signup } from '@/stores/auth/index';
import type { SignupRequest } from '@/stores/auth/schema';
import Dropdown from 'primevue/dropdown';
import InputMask from 'primevue/inputmask';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import { useToast } from 'primevue/usetoast';
import { useRouter } from 'vue-router';
import { ApiError } from '../../utils/types';
import SignupCommentsModal from '@/components/signup/SignupCommentsModal.vue';
import { DEFAULT_SERVER_ERROR } from '../../consts';
import { showError, showSuccess } from '@/utils/errorUtils';
import {
  loading,
  hasSucceeded,
  signupCommentsModalOpen,
  userForm,
  countries,
  roles,
} from './index';

const router = useRouter();
const toast = useToast();

const submitSignup = async (userForm: SignupRequest) => {
  loading.value = true;
  try {
    await signup(userForm);
    hasSucceeded.value = true;
    setTimeout(
      () =>
        showSuccess(
          toast,
          'You have successfully signed up! An administrator will review your registration and confirm or reject it.'
        ),
      100
    );
    router.push({ name: 'Login' });
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="flex h-full w-full items-center justify-center bg-slate-100">
    <SignupCommentsModal
      :visible="signupCommentsModalOpen"
      @continueSignup="submitSignup(userForm)"
      @closeModal="signupCommentsModalOpen = false"
    />
    <PageForm
      heading="Sign up"
      name="Signup"
      formLabel="Signup"
      @submit="signupCommentsModalOpen = true"
      data-testid="signup-form"
    >
      <template #default>
        <!-- name -->
        <InputText
          id="name"
          v-model="userForm.name"
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
          toggleMask
        >
        </Password>
        <!-- Phone -->
        <InputMask
          id="Phone"
          v-model="userForm.phone"
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
        <!-- <SignupCommentsModal
          :visible="signupCommentsModalOpen"
          @closeModal="signupCommentsModalOpen = false"
          @continueSignup="
            (comments) => {
              userForm.comments = comments;
              signupCommentsModalOpen = false;
              submitSignup();
            }
          "
        /> -->
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
