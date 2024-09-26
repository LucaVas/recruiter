<script setup lang="ts">
import { ref } from 'vue';
import Dropdown from 'primevue/dropdown';
import type { Questionnaire } from '@/types/questionnaireTypes';

// variables
const loading = ref(false);
const selectedQuestionnaire = ref<Questionnaire>();

// props
const { questionnaires } = defineProps<{
  questionnaires: Questionnaire[];
}>();

// emits
defineEmits<{
  (e: 'selectQuestionnaire', questionnaire: Questionnaire): void;
}>();
</script>

<template>
  <div class="card justify-content-center flex">
    <Dropdown
      :loading="loading"
      @update:modelValue="(e) => $emit('selectQuestionnaire', e)"
      v-model="selectedQuestionnaire"
      :options="questionnaires"
      filter
      optionLabel="title"
      placeholder="Select a Questionnaire"
      class="w-full"
    >
      <template #value="slotProps">
        <div v-if="slotProps.value" class="align-items-center flex">
          <div>{{ slotProps.value.title }}</div>
        </div>
      </template>
    </Dropdown>
  </div>
</template>
