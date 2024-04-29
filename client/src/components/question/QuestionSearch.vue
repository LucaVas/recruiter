<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-row gap-3">
      <InputText
        id="question-input"
        v-model="clientOrSkill"
        @input="searchQuestion(clientOrSkill)"
      />
      <Button label="New Question" @click="$emit('createNewQuestion')" />
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
