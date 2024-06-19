<script setup lang="ts">
import IconField from 'primevue/iconfield';
import InputIcon from 'primevue/inputicon';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { ref } from 'vue';

const emits = defineEmits<{
  (e: 'search', identifier: string): void;
  (e: 'openNewCandidateModal'): void;
}>();

const { searching } = defineProps<{
  searching: boolean;
}>();

const identifier = ref('');
</script>

<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-col gap-2 sm:flex-row">
      <IconField class="w-full">
        <InputIcon>
          <i class="pi pi-search" />
        </InputIcon>
        <InputText
          id="identifier"
          v-model="identifier"
          type="text"
          class="w-full"
          placeholder="Candidate PAN, phone or email"
          required
        />
      </IconField>
      <Button
        label="Search"
        size="small"
        class="w-full sm:w-[10rem]"
        :loading="searching"
        @click="emits('search', identifier)"
        :disabled="identifier === ''"
      />
      <Button
        size="small"
        label="New candidate"
        icon="pi pi-user-plus"
        iconPos="right"
        class="min-w-fit"
        outlined
        @click="emits('openNewCandidateModal')"
      />
    </div>
  </div>
</template>
