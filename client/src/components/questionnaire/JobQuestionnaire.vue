<script setup lang="ts">
import Button from 'primevue/button';
import QuestionCard from './QuestionCard.vue';
import InputText from 'primevue/inputtext';
import { ref } from 'vue';
import type { NewQuestion, NewQuestionnaire, Questionnaire } from '@/stores/question/schema';
import { v4 } from 'uuid';

const { questionnaire } = defineProps<{ questionnaire: Questionnaire }>();

const content = ref(questionnaire);

const questions = ref<{ localId: string; question: NewQuestion }[]>(
  questionnaire.questions
    ? questionnaire.questions.map((q) => ({ localId: v4(), question: q }))
    : []
);

const createQuestion = () => {
  questions.value.push({
    localId: v4(),
    question: {
      text: '',
      answer: '',
      questionType: 'OPEN_QUESTION',
    },
  });
};

const removeQuestion = (localId: string) => {
  questions.value = questions.value.filter((q) => q.localId !== localId);

  emits('updateQuestionnaire', {
    ...content.value,
    questions: questions.value.map((q) => q.question),
  });
};

const emits = defineEmits<{
  (e: 'updateQuestionnaire', questionnaire: NewQuestionnaire): void;
}>();
</script>

<template>
  <div>
    <label class="text-md">Questionnaire</label>
    <div class="mt-3 flex gap-4">
      <InputText
        class="w-full"
        placeholder="Questionnaire Title"
        :modelValue="questionnaire.title"
        @update:modelValue="
          (t) => {
            content.title = t ?? '';
            $emit('updateQuestionnaire', {
              ...content,
              questions: questions.map((q) => q.question),
            });
          }
        "
      />
      <Button
        icon="pi pi-plus"
        class="hidden min-w-fit md:flex"
        outlined
        label="New"
        @click="createQuestion()"
      />
      <Button icon="pi pi-plus" class="min-w-fit md:hidden" outlined @click="createQuestion()" />
    </div>

    <div class="mt-3 space-y-3">
      <QuestionCard
        :question="item.question"
        @remove="removeQuestion(item.localId)"
        v-for="item in questions"
        :key="item.localId"
        @updateQuestion="
          (q) => {
            item.question.text = q;
            $emit('updateQuestionnaire', {
              ...content,
              questions: questions.map((q) => q.question),
            });
          }
        "
        @updateAnswer="
          (a) => {
            item.question.answer = a;
            $emit('updateQuestionnaire', {
              ...content,
              questions: questions.map((q) => q.question),
            });
          }
        "
        @updateType="
          (t) => {
            item.question.questionType = t;
            $emit('updateQuestionnaire', {
              ...content,
              questions: questions.map((q) => q.question),
            });
          }
        "
      />
    </div>
  </div>
</template>
