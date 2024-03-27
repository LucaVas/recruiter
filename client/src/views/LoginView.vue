<script lang="ts" setup>
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import PageForm from '@/components/PageForm.vue';
import { useRouter } from 'vue-router';
import FloatLabel from 'primevue/floatlabel';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import Message from 'primevue/message';
import useErrorMessage from '../composables/';

const router = useRouter();
const loading = ref(false);
const store = useUserStore();

const userForm = ref({
  usernameOrEmail: '',
  password: '',
});

const [submitLogin, errorMessage] = useErrorMessage(async () => {
  loading.value = true;
  await store.login(userForm.value);

  router.push({ name: 'Signup' });
});
</script>

<template>
  <div class="flex h-screen w-full justify-center bg-slate-100">
    <PageForm heading="Log in" formLabel="Login" @submit="submitLogin" data-testid="login-form">
      <template #default>
        <!-- email -->
        <FloatLabel class="flex flex-col">
          <InputText
            type="email"
            id="email"
            v-model="userForm.usernameOrEmail"
            minlength="3"
            maxlength="50"
            required
          />
          <label for="email">Email</label>
        </FloatLabel>

        <Password
          v-model="userForm.password"
          :feedback="false"
          placeholder="Password"
          minlength="8"
          maxlength="64"
          class="flex flex-col"
          required
        />

        <Message
          v-if="errorMessage"
          severity="error"
          :sticky="false"
          :life="5000"
          :closable="false"
          data-testid="error-message"
        >
          {{ errorMessage }}
        </Message>

        <div class="grid grid-flow-row auto-rows-max gap-2">
          <Button
            type="submit"
            label="Login"
            :loading="loading && !errorMessage"
            :disabled="loading && !errorMessage"
            class="flex items-center justify-center"
          />
        </div>
      </template>

      <template #footer>
        <div class="flex items-center justify-between">
          <RouterLink :to="{ name: 'Login' }" target="_blank" rel="noopener">
            <Button
              class="w-full"
              type="button"
              label="Forgot password?"
              :loading="loading && !errorMessage"
              :disabled="loading && !errorMessage"
              data-testid="reset-password-button"
            />
          </RouterLink>
          <Message
            class="text-center"
            severity="secondary"
            :sticky="true"
            :closable="false"
            data-testid="not-member-message"
          >
            <RouterLink :to="{ name: 'Signup' }" class="font-semibold leading-6"
              >Not a member?</RouterLink
            >
          </Message>
        </div>
      </template>
    </PageForm>
  </div>
</template>
