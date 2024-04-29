<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-row gap-3 w-full">
      <InputText
        id="question-input"
        v-model="clientOrSkill"
        @input="searchQuestion(clientOrSkill)"
        class="w-full"
      />
      <Button label="New" icon="pi pi-plus" @click="$emit('createNewQuestion')" class="min-w-fit" />
    </div>
    <small id="question-input-help"
      >Type companies or skills related to the question you want to add.</small
    >
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

function searchQuestion(clientOrSkill: string) {
  console.log(clientOrSkill);
}

defineEmits<{
  (e: 'createNewQuestion'): void;
}>();

const clientOrSkill = ref('');
</script>
