<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import QuestionCard from './QuestionCard.vue';
import { type Questionnaire, type QuestionnaireDto } from '@/stores/questionnaire/schema';
import { ref } from 'vue';
import { handleError } from '@/utils/errorUtils';
import { useToast } from 'primevue/usetoast';
import { saveNewQuestionnaire, updateQuestionnaire } from '../../stores/questionnaire/api';
import type { Client } from '@/stores/client/schema';
import { v4 } from 'uuid';
import type { NewQuestion, Question } from '@/stores/question/schema';

const props = defineProps<{
  visible: boolean;
  isUpdate: boolean;
  questionnaire: Questionnaire | QuestionnaireDto;
  client: Client;
}>();
const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'select', questionnaire: Questionnaire): void;
}>();

const creatingOrUpdating = ref(false);
const toast = useToast();

const emptyQuestionnaire: Questionnaire = {
  title: '',
  questions: [],
  client: props.client,
};
const tmpQuestionnaire = ref(emptyQuestionnaire);

const createQuestion = () => {
  tmpQuestionnaire.value.questions.push({
    id: v4(),
    text: '',
    answer: '',
    questionType: 'OPEN_QUESTION',
  });
};
const removeQuestion = (question: Question) => {
  console.log(question)
  tmpQuestionnaire.value.questions = tmpQuestionnaire.value.questions.filter(
    (q) => q.id !== question.id
  );
};

const updateQuestion = (q: Question) => {
  tmpQuestionnaire.value.questions = tmpQuestionnaire.value.questions.map((question) =>
    question.id === q.id ? q : question
  );
};

const create = async (questionnaire: Questionnaire) => {
  creatingOrUpdating.value = true;
  try {
    // remove the id from questions
    const questions: NewQuestion[] = questionnaire.questions.map((q) => {
      const { id, ...rest } = q;
      return rest;
    });
    const newQuestionnaire = await saveNewQuestionnaire({
      ...questionnaire,
      questions,
    });
    console.log(newQuestionnaire);
    emits('select', newQuestionnaire);
  } catch (err) {
    handleError(toast, err);
  } finally {
    creatingOrUpdating.value = false;
  }
};
const update = async (questionnaire: Questionnaire) => {
  creatingOrUpdating.value = true;
  try {
    const updatedQuestionnaire = await updateQuestionnaire(
      (props.questionnaire as QuestionnaireDto).id,
      questionnaire
    );
    emits('select', updatedQuestionnaire);
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
      @show="
        props.isUpdate
          ? (tmpQuestionnaire = props.questionnaire)
          : (tmpQuestionnaire = {
              ...emptyQuestionnaire,
              client: client,
            })
      "
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
            :model-value="props.questionnaire.title"
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
            v-for="question in tmpQuestionnaire.questions"
            :key="question.id"
            :question="question"
            @remove="removeQuestion(question)"
            @update="(q) => updateQuestion(q)"
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
            :label="props.isUpdate ? 'Save' : 'Create'"
            @click="props.isUpdate ? update(tmpQuestionnaire) : create(tmpQuestionnaire)"
            :loading="creatingOrUpdating"
            :disabled="creatingOrUpdating"
          />
        </div>
      </div>
    </Dialog>
  </div>
</template>
