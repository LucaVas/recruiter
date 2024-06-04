<script setup lang="ts">
import { type Questionnaire } from '@/stores/questionnaire/schema';
import Panel from 'primevue/panel';

const { questionnaire } = defineProps<{
  questionnaire: Questionnaire;
}>();
</script>

<template>
  <Panel class="w-full" header="Questionnaire" toggleable collapsed>
    <p class="m-0">
      {{ questionnaire }}
    </p>
  </Panel>
</template>
