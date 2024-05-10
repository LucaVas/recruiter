<template>
  <div class="flex items-center justify-center">
    <Dialog
      :visible="visible"
      @update:visible="
        {
          $emit('close');
          initSearch();
        }
      "
      closeOnEscape
      modal
      class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
    >
      <div>
        <div class="flex w-full flex-row gap-3">
          <InputGroup class="flex w-full flex-row">
            <InputText
              autofocus
              id="question-input"
              v-model="titleOrClient"
              @keypress="
                ($event) => {
                  if ($event.key === 'Enter' && titleOrClient !== '') {
                    search(titleOrClient);
                  }
                }
              "
              class="w-full"
            />
            <Button
              icon="pi pi-search"
              @click="titleOrClient !== '' ? search(titleOrClient) : null"
            />
          </InputGroup>
        </div>
        <small id="question-input-help" v-show="titleOrClient === ''" class="text-gray-500"
          >Type question title or client name</small
        >
      </div>
      <div class="mt-3 h-72 overflow-auto">
        <div v-if="questions.length > 0" class="space-y-2">
          <QuestionSearchModalPanel
            v-for="question in questions"
            :key="question.id"
            :question="question"
            @selectOrUnselectQuestion="(question) => selectedQuestions.push(question)"
          />
        </div>
        <div v-else>No questions found or searched yet.</div>
      </div>
      <template #footer>
        <div class="flex justify-end gap-2">
          <Button
            label="Cancel"
            class="min-w-fit"
            @click="
              {
                $emit('close');
                initSearch();
              }
            "
            icon="pi pi-times"
            size="small"
            outlined
          />
          <Button
            label="Add"
            class="min-w-fit"
            @click="
              {
                $emit('selectOrUnselectQuestions', selectedQuestions);
                initSearch();
              }
            "
            icon="pi pi-check"
            size="small"
          />
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import Button from 'primevue/button';
import { ref } from 'vue';
import { type Question } from '@/stores/question/schema';
import { searchQuestions } from '@/stores/question';
import { useToast } from 'primevue/usetoast';
import QuestionSearchModalPanel from '@/components/question/QuestionSearchModalPanel.vue';
import { ApiError } from '@/utils/types';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();

const { visible, questionsSelected } = defineProps<{
  visible: boolean;
  questionsSelected: Question[];
}>();

defineEmits<{
  (e: 'selectOrUnselectQuestions', selectedQuestions: Question[]): void;
  (e: 'close'): void;
}>();

async function search(titleOrClient: string) {
  searchingQuestions.value = true;
  try {
    const found = await searchQuestions(titleOrClient);
    if (questionsSelected) {
      questions.value = found.filter(
        (question) => !questionsSelected.some((selected) => selected.id === question.id)
      );
    } else {
      questions.value = found;
    }
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    searchingQuestions.value = false;
  }
}

const initSearch = () => {
  titleOrClient.value = '';
  selectedQuestions.value = [];
  questions.value = [];
};

const searchingQuestions = ref(false);
const questions = ref<Question[]>([]);
const selectedQuestions = ref<Question[]>([]);
const titleOrClient = ref('');
</script>
