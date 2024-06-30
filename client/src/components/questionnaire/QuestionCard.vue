<script setup lang="ts">
import Textarea from 'primevue/textarea';
import Dropdown from 'primevue/dropdown';
import RadioButton from 'primevue/radiobutton';
import Button from 'primevue/button';
import { questionTypes } from './questionTypes';
import type { Question, QuestionType } from '@/stores/question/schema';
import { ref } from 'vue';

const props = defineProps<{
  question: Question;
}>();

const text = ref(props.question.text);
const questionType = ref<QuestionType>(props.question.questionType);
const answer = ref<string | null>(props.question.answer);

const updateQuestion = () => {
  emits('update', {
    id: props.question.id,
    text: text.value,
    questionType: questionType.value,
    answer: answer.value,
  });
};

const emits = defineEmits<{
  (e: 'remove'): void;
  (e: 'update', question: Question): void;
}>();
</script>

<template>
  <div class="space-y-4 border bg-slate-100 p-4">
    <div class="flex flex-col items-center gap-4 sm:flex-row">
      <InputText
        class="w-full"
        placeholder="Write your question here"
        v-model="text"
        @update:model-value="updateQuestion()"
      />
      <div class="flex w-full justify-between gap-3 sm:w-1/3">
        <Dropdown
          class="w-full md:min-w-fit"
          placeholder="Select a type"
          v-model="questionType"
          :options="questionTypes"
          @update:model-value="updateQuestion()"
          option-label="label"
          option-value="value"
        />
        <Button
          icon="pi pi-trash"
          class="p-panel-header-icon min-w-fit"
          @click="$emit('remove')"
          unstyled
        />
      </div>
    </div>
    <div v-if="questionType !== 'OPEN_QUESTION'">
      <Textarea
        v-if="questionType === 'SHORT'"
        placeholder="Short-answer text"
        style="resize: none"
        rows="1"
        cols="30"
        class="w-full"
        v-model="answer"
        @update:model-value="updateQuestion()"
      />
      <Textarea
        v-if="questionType === 'PARAGRAPH'"
        placeholder="Long-answer text"
        rows="5"
        cols="30"
        class="w-full"
        v-model="answer"
        @update:model-value="updateQuestion()"
      />
      <div v-if="questionType === 'YES_NO'" class="flex gap-3">
        <div class="align-center flex">
          <RadioButton
            v-model="answer"
            inputId="yes"
            name="yes"
            value="Yes"
            @change="updateQuestion()"
          />
          <label for="yes" class="ml-2">Yes</label>
        </div>
        <div class="align-center flex">
          <RadioButton
            v-model="answer"
            inputId="no"
            name="no"
            value="No"
            @change="updateQuestion()"
          />
          <label for="no" class="ml-2">No</label>
        </div>
      </div>
    </div>
  </div>
</template>
