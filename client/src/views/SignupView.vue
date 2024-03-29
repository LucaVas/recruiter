<script lang="ts" setup>
import { ref } from 'vue';
import PageForm from '@/components/PageForm.vue';
import FloatLabel from 'primevue/floatlabel';
import InputText from 'primevue/inputtext';
import useErrorMessage from '@/composables';
import InputMask from 'primevue/inputmask';
import Dropdown from 'primevue/dropdown';
import { useUserStore } from '@/stores/user';
import { type UserSignupForm } from '@/stores/user/types';
import Password from 'primevue/password';

const store = useUserStore();
const countries = ref([{ label: 'India', value: 'india' }]);
const userForm = ref<UserSignupForm>({
  name: '',
  username: '',
  email: '',
  password: '',
  mobile: '',
  city: '',
  country: '',
});

const hasSucceeded = ref(false);
const loading = ref(false);

const [submitSignup, errorMessage] = useErrorMessage(async () => {
  loading.value = true;
  console.log(userForm.value);
  await store.signup(userForm.value);
  loading.value = false;
  hasSucceeded.value = true;
});
</script>

<template>
  <div class="flex h-full w-full justify-center bg-slate-100">
    <PageForm
      heading="Sign up"
      name="Signup"
      formLabel="Signup"
      @submit="submitSignup"
      data-testid="signup-form"
    >
      <template #default>
        <!-- name -->
        <FloatLabel class="flex flex-col">
          <InputText id="name" v-model="userForm.name" minlength="3" maxlength="50" required />
          <label for="name">Name</label>
        </FloatLabel>
        <!-- username -->
        <FloatLabel class="flex flex-col">
          <InputText
            id="username"
            v-model="userForm.username"
            minlength="3"
            maxlength="50"
            required
          />
          <label for="username">Username</label>
        </FloatLabel>
        <!-- email -->
        <FloatLabel class="flex flex-col">
          <InputText
            type="email"
            id="email"
            v-model="userForm.email"
            minlength="3"
            maxlength="50"
            required
          />
          <label for="email">Email</label>
        </FloatLabel>
        <!-- password -->
        <Password
          v-model="userForm.password"
          class="flex flex-col"
          placeholder="Password"
          id="password"
          minlength="8"
          maxlength="64"
          required
        >
          <template #header>
            <h6>Pick a password</h6>
          </template>
          <template #footer>
            <Divider />
            <p class="mb-2 mt-2 p-0">Suggestions</p>
            <ul class="m-0 ml-2 list-disc p-0 pl-2 leading-6" style="line-height: 1.5">
              <li>At least one lowercase</li>
              <li>At least one uppercase</li>
              <li>At least one numeric</li>
              <li>Minimum 8 characters</li>
            </ul>
          </template>
        </Password>
        <!-- mobile -->
        <FloatLabel class="flex flex-col">
          <InputMask
            id="phone"
            v-model="userForm.mobile"
            mask="(999) 999-9999"
            placeholder="(999) 999-9999"
            :unmask="true"
          />
          <label for="phone">Phone</label>
        </FloatLabel>
        <!-- city -->
        <FloatLabel class="flex flex-col">
          <InputText id="city" v-model="userForm.city" minlength="3" maxlength="50" required />
          <label for="city">City</label>
        </FloatLabel>
        <!-- country -->
        <Dropdown
          v-model="userForm.country"
          editable
          :options="countries"
          optionLabel="label"
          optionValue="value"
          placeholder="Country"
          class="md:w-14rem w-full"
        />

        <!-- comments -->

        <Message
          v-if="hasSucceeded"
          severity="success"
          :sticky="true"
          data-testid="success-message"
        >
          You have successfully signed up! An administrator will review your registration and
          confirm or reject it.
        </Message>

        <Message
          v-if="errorMessage"
          severity="error"
          :sticky="false"
          :life="5000"
          data-testid="error-message"
        >
          {{ errorMessage }}
        </Message>

        <div class="grid gap-2">
          <Button
            type="submit"
            color="red"
            label="Sign up"
            :loading="loading && !errorMessage"
            :disabled="loading"
          />
        </div>
      </template>
      <template #footer>
        <div>
          <Message
            class="bg-transparent text-center"
            severity="info"
            :sticky="true"
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
