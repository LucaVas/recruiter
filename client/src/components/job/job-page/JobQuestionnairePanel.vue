<script setup lang="ts">
import { type Questionnaire } from '@/stores/questionnaire/schema';
import Panel from 'primevue/panel';
import { ref } from 'vue';

const { questionnaire } = defineProps<{
  questionnaire: Questionnaire;
}>();

const loadingQuestionnaire = ref(false);
</script>

<template>
  <Panel class="w-full" header="Questionnaire" toggleable :collapsed="false">
    <div v-if="loadingQuestionnaire" class="flex w-full items-center justify-center">
      <ProgressSpinner />
    </div>
    <div v-else class="space-y-4">
      <div
        v-for="question in questionnaire.questions"
        :key="questionnaire.questions.indexOf(question)"
        class=""
      >
        <p class="font-semibold">
          Q{{ questionnaire.questions.indexOf(question) + 1 }}: {{ question.text }}
        </p>
        <p v-if="question.answer">A: {{ question.answer }}</p>
      </div>
    </div>
  </Panel>
</template>
