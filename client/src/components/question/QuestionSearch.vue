<template>
  <div class="flex flex-col gap-2">
    <div class="flex w-full flex-row gap-3">
      <InputGroup class="flex w-full flex-row">
        <InputText
          id="question-input"
          v-model="clientOrSkill"
          @keypress="
            ($event) => {
              if ($event.key === 'Enter' && clientOrSkill !== '') {
                $emit('searchQuestions', clientOrSkill);
              }
            }
          "
          class="w-full"
        />
        <Button
          outlined
          icon="pi pi-search"
          @click="clientOrSkill !== '' ? $emit('searchQuestions', clientOrSkill) : null"
        />
      </InputGroup>

      <Button
        label="New"
        icon="pi pi-plus"
        @click="$emit('createNewQuestion')"
        class="hidden min-w-fit md:block"
      />
      <Button icon="pi pi-plus" @click="$emit('createNewQuestion')" class="min-w-fit md:hidden" />
    </div>
    <small id="question-input-help" v-show="clientOrSkill === ''" class="text-gray-500"
      >Type client name or skill related to the questions you want to add.</small
    >
  </div>
</template>

<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import { ref } from 'vue';

defineEmits<{
  (e: 'createNewQuestion'): void;
  (e: 'searchQuestions', clientOrSkill: string): void;
}>();

const clientOrSkill = ref('');
</script>
