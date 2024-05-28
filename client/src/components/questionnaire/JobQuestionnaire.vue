<script setup lang="ts">
import Button from 'primevue/button';
import QuestionCard from './QuestionCard.vue';
import { ref } from 'vue';
import type { NewQuestion, NewQuestionnaire } from '@/stores/question/schema';

const questions = ref<NewQuestion[]>([]);
const title = ref('');

const createQuestion = () => {
  questions.value.push({
    text: '',
    answer: '',
    questionType: 'OPEN_QUESTION',
  });
};

const removeQuestion = (index: number) => {
  questions.value.splice(index, 1);
  emits('updateQuestionnaire', {
    title: title.value,
    questions: questions.value,
  });
};

const emits = defineEmits<{
  (e: 'updateQuestionnaire', questionnaire: NewQuestionnaire): void;
}>();
</script>

<template>
  <div>
    <label class="text-md">Questionnaire</label>
    <div class="flex gap-4 mt-3">
      <TextInput
        :model="title"
        @input="
          (t: string) => {
            title = t;
            $emit('updateQuestionnaire', {
              title,
              questions,
            });
          }
        "
        placeholder="Questionnaire Title"
      />
      <Button icon="pi pi-plus" outlined label="New" @click="createQuestion()" />
    </div>
    
    <div class="mt-3 space-y-3">
      <QuestionCard
        :text="question.text"
        :answer="question.answer"
        :index="questions.indexOf(question)"
        @remove="removeQuestion(questions.indexOf(question))"
        v-for="question in questions"
        :key="questions.indexOf(question)"
        @updateQuestion="
          (q) => {
            question.text = q;
            $emit('updateQuestionnaire', {
              title,
              questions,
            });
          }
        "
        @updateAnswer="
          (a) => {
            question.answer = a;
            $emit('updateQuestionnaire', {
              title,
              questions,
            });
          }
        "
        @updateType="
          (t) => {
            question.questionType = t;
            $emit('updateQuestionnaire', {
              title,
              questions,
            });
          }
        "
      />
    </div>
  </div>
</template>
