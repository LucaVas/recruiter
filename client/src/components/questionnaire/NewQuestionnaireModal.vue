<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import QuestionCard from './QuestionCard.vue';
import { type NewQuestionnaire, type Questionnaire } from '@/stores/questionnaire/schema';
import { ref } from 'vue';
import { handleError } from '@/utils/errorUtils';
import { useToast } from 'primevue/usetoast';
import type { NewQuestion } from '@/stores/question/schema';
import { v4 } from 'uuid';
import { saveNewQuestionnaire, updateQuestionnaire } from '../../stores/questionnaire/api';
import type { Client } from '@/stores/client/schema';

const { visible, questionnaire, client } = defineProps<{
  visible: boolean;
  questionnaire?: Questionnaire;
  client: Client;
}>();
const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'select', questionnaire: Questionnaire): void;
}>();

const tmpQuestionnaire = questionnaire
  ? ref(questionnaire)
  : ref({ title: '', clientName: '', questions: [] });
const creatingOrUpdating = ref(false);
const toast = useToast();
const questions = ref<{ localId: string; question: NewQuestion }[]>(
  tmpQuestionnaire.value.questions
    ? tmpQuestionnaire.value.questions.map((q) => ({ localId: v4(), question: q }))
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
};

const init = () => {
  tmpQuestionnaire.value = {
    title: '',
    clientName: '',
    questions: [],
  };
  questions.value = [];
};

const create = async (questionnaire: NewQuestionnaire) => {
  creatingOrUpdating.value = true;
  try {
    questionnaire.questions = questions.value.map((q) => q.question);
    questionnaire.clientName = client.name;
    const newQuestionnaire = await saveNewQuestionnaire(questionnaire);
    console.log(newQuestionnaire);
    init();
    emits('select', newQuestionnaire);
    // await createJob(job);
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingOrUpdating.value = false;
  }
};
const update = async (questionnaire: Questionnaire) => {
  creatingOrUpdating.value = true;
  try {
    questionnaire.questions = questions.value.map((q) => q.question);
    const updatedQuestionnaire = await updateQuestionnaire(questionnaire.title, questionnaire);
    init();
    emits('select', updatedQuestionnaire);
    // await createJob(job);
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingOrUpdating.value = false;
  }
};
</script>

<template>
  <div class="flex justify-center">
    <Dialog
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="Questionnaire"
      :style="{ width: '50rem' }"
      :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
      maximizable
    >
      <div>
        <div class="mt-3 flex gap-4">
          <InputText
            class="w-full"
            placeholder="Questionnaire Title"
            :modelValue="tmpQuestionnaire.title"
            @update:modelValue="(t) => (tmpQuestionnaire.title = t ?? '')"
          />
          <Button
            icon="pi pi-plus"
            class="hidden min-w-fit md:flex"
            outlined
            label="New"
            @click="createQuestion()"
          />
          <Button
            icon="pi pi-plus"
            class="min-w-fit md:hidden"
            outlined
            @click="createQuestion()"
          />
        </div>

        <div class="mt-3 space-y-3">
          <QuestionCard
            :question="item.question"
            @remove="removeQuestion(item.localId)"
            v-for="item in questions"
            :key="item.localId"
            @updateQuestion="(q) => (item.question.text = q)"
            @updateAnswer="(a) => (item.question.answer = a)"
            @updateType="(t) => (item.question.questionType = t)"
          />
        </div>
      </div>
      <Divider />

      <div class="mt-5 flex flex-col gap-4 sm:flex-row sm:items-center">
        <span class="min-w-fit font-semibold">Client: {{ client.name }}</span>
        <div class="flex w-full justify-between gap-4 sm:justify-end">
          <Button
            label="Cancel"
            severity="secondary"
            @click="$emit('close')"
            :loading="creatingOrUpdating"
            :disabled="creatingOrUpdating"
          />
          <Button
            :label="questionnaire ? 'Save' : 'Create'"
            @click="questionnaire ? update(tmpQuestionnaire) : create(tmpQuestionnaire)"
            :loading="creatingOrUpdating"
            :disabled="creatingOrUpdating"
          />
        </div>
      </div>
    </Dialog>
  </div>
</template>
