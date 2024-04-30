<template>
  <div class="flex items-center justify-center">
    <Dialog
      :visible="visible"
      @update:visible="
        {
          $emit('close');
          clientOrSkill = '';
          selectedQuestions = [];
          questions = [];
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
              id="question-input"
              v-model="clientOrSkill"
              @keypress="
                ($event) => {
                  if ($event.key === 'Enter' && clientOrSkill !== '') {
                    search(clientOrSkill);
                  }
                }
              "
              class="w-full"
            />
            <Button
              outlined
              icon="pi pi-search"
              @click="clientOrSkill !== '' ? search(clientOrSkill) : null"
            />
          </InputGroup>
        </div>
        <small id="question-input-help" v-show="clientOrSkill === ''" class="text-gray-500"
          >Type client name or skill related to the questions you want to add.</small
        >
      </div>
      <div class="mt-3 h-72 space-y-2 overflow-auto">
        <div v-if="questions.length > 0">
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
                clientOrSkill = '';
                selectedQuestions = [];
                questions = [];
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
                clientOrSkill = '';
                selectedQuestions = [];
                questions = [];
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

const toast = useToast();

const { visible, questionsSelected } = defineProps<{
  visible: boolean;
  questionsSelected: Question[];
}>();

defineEmits<{
  (e: 'selectOrUnselectQuestions', selectedQuestions: Question[]): void;
  (e: 'close'): void;
}>();

async function search(clientOrSkill: string) {
  searchingQuestions.value = true;
  try {
    const found = await searchQuestions(clientOrSkill);
    if (questionsSelected) {
      questions.value = found.filter(
        (question) => !questionsSelected.some((selected) => selected.id === question.id)
      );
    } else {
      questions.value = found;
    }
  } catch (e) {
    showError(e as string);
  } finally {
    searchingQuestions.value = false;
  }
}

const showError = (message: string) => {
  toast.add({
    severity: 'error',
    summary: 'Error',
    detail: message,
    life: 3000,
  });
};

const searchingQuestions = ref(false);
const questions = ref<Question[]>([]);
const selectedQuestions = ref<Question[]>([]);
const clientOrSkill = ref('');
</script>
