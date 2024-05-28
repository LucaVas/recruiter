<script setup lang="ts">
import TextInput from '@/components/shared/TextInput.vue';
import Textarea from 'primevue/textarea';
import Dropdown from 'primevue/dropdown';
import RadioButton from 'primevue/radiobutton';
import Button from 'primevue/button';
import { ref } from 'vue';
import { questionTypes } from './questionTypes';
import type { QuestionType } from '@/stores/question/schema';

const { text, answer } = defineProps<{
  text: string;
  answer: string;
}>();

const question = ref(text);
const type = ref<QuestionType>('OPEN_QUESTION');
const shortAnswer = ref(answer);
const longAnswer = ref(answer);
const radioButton = ref(answer);

defineEmits<{
  (e: 'remove'): void;
  (e: 'updateAnswer', answer: string): void;
  (e: 'updateQuestion', question: string): void;
  (e: 'updateType', type: QuestionType): void;
}>();
</script>

<template>
  <div class="space-y-4 border p-4 bg-slate-100">
    <div class="flex flex-col items-center gap-4 sm:flex-row">
      <TextInput
      class="w-full"
        :model="question"
        @input="(q) => $emit('updateQuestion', q)"
        placeholder="Write your question here"
      />
      <div class="w-full sm:w-1/3 flex justify-between gap-3">
        <Dropdown
          class="w-full md:min-w-fit"
          placeholder="Select a type"
          v-model="type"
          :options="questionTypes"
          @change="$emit('updateType', type)"
          option-label="label"
          option-value="value"
        />
        <Button icon="pi pi-trash" class="p-panel-header-icon min-w-fit" @click="$emit('remove')" unstyled />
      </div>
    </div>
    <Textarea
      v-if="type === 'SHORT'"
      placeholder="Short-answer text"
      v-model="shortAnswer"
      style="resize: none"
      rows="1"
      cols="30"
      class="w-full"
      @input="$emit('updateAnswer', shortAnswer)"
    />
    <Textarea
      v-if="type === 'PARAGRAPH'"
      placeholder="Long-answer text"
      v-model="longAnswer"
      rows="5"
      cols="30"
      class="w-full"
      @input="$emit('updateAnswer', longAnswer)"
    />
    <div v-if="type === 'YES_NO'" class="flex gap-3">
      <div class="align-center flex">
        <RadioButton
          v-model="radioButton"
          inputId="yes"
          name="yes"
          value="Yes"
          @change="$emit('updateAnswer', radioButton)"
        />
        <label for="yes" class="ml-2">Yes</label>
      </div>
      <div class="align-center flex">
        <RadioButton
          v-model="radioButton"
          inputId="no"
          name="no"
          value="No"
          @change="$emit('updateAnswer', radioButton)"
        />
        <label for="no" class="ml-2">No</label>
      </div>
    </div>
  </div>
</template>
