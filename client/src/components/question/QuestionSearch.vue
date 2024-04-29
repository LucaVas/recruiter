<template>
  <div class="flex-col flex gap-2">
    <InputText id="question-input" v-model="clientOrSkill" @input="searchQuestion(clientOrSkill)" />
    <small id="question-input-help">Type companies or skills related to the question you want to add.</small>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

function searchQuestion(clientOrSkill: string) {
  console.log(clientOrSkill);
}

const clientOrSkill = ref('');
</script>
