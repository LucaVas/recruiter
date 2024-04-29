<template>
  <div class="flex flex-col gap-2">
    <div class="flex w-full flex-row gap-3">
      <InputText
        id="question-input"
        v-model="clientOrSkill"
        @keypress="($event) => $event.key === 'Enter' ? $emit('searchQuestions', clientOrSkill) : null"
        class="w-full"
      />
      <Button label="New" icon="pi pi-plus" @click="$emit('createNewQuestion')" class="min-w-fit" />
    </div>
    <small id="question-input-help"
      >Type client name or skill related to the questions you want to add.</small
    >
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

defineEmits<{
  (e: 'createNewQuestion'): void;
  (e: 'searchQuestions', clientOrSkill: string): void;
}>();

const clientOrSkill = ref('');
</script>
