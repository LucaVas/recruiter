<script setup lang="ts">
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import { ref } from 'vue';
import { type Questionnaire } from '@/stores/questionnaire/schema';
import { getQuestionnairesByClient } from '@/stores/questionnaire/api';
import { useToast } from 'primevue/usetoast';
import { handleError } from '@/utils/errorUtils';

const toast = useToast();
const selectedQuestionnaire = ref<Questionnaire | null>(null);
const emits = defineEmits<{
  (e: 'select', selectedQuestionnaire: Questionnaire | null): void;
}>();

const clientOrTitle = ref('');
const questionnaires = ref<Questionnaire[]>([]);

const getQuestionnaires = async (clientName: string) => {
  try {
    questionnaires.value = await getQuestionnairesByClient(clientName);
  } catch (err) {
    handleError(toast, err);
  }
};
</script>

<template>
  <div class="mb-5 flex flex-col gap-2">
    <label class="text-md">Questionnaire</label>
    <div>
      <div class="flex w-full flex-row gap-3">
        <InputGroup class="flex w-full flex-row">
          <InputText
            autofocus
            v-model="clientOrTitle"
            @keypress="
              ($event) => {
                if ($event.key === 'Enter' && clientOrTitle !== '') {
                  getQuestionnaires(clientOrTitle);
                }
              }
            "
            class="w-full"
          />
          <Button
            icon="pi pi-search"
            @click="clientOrTitle !== '' ? getQuestionnaires(clientOrTitle) : null"
          />
        </InputGroup>
      </div>
      <small id="question-input-help" v-show="clientOrTitle === ''" class="text-gray-500"
        >Type questionnaire title or client name</small
      >
    </div>
  </div>

  <div>
    <DataTable
      :value="questionnaires"
      scrollable
      scrollHeight="200px"
      tableStyle="min-width: 50rem"
      :virtualScrollerOptions="{
        lazy: true,
        itemSize: 46,
        delay: 200,
        showLoader: true,
        numToleratedItems: 10,
      }"
    >
      <Column header="Action">
        <template #body="slotProps">
          <Button label="Select" unstyled class="text-blue-500" @click="selectedQuestionnaire = slotProps.data" />
        </template>
      </Column>
      <Column header="Client">
        <template #body="slotProps"> {{ slotProps.data.clientName }} </template>
      </Column>
      <Column header="Title">
        <template #body="slotProps"> {{ slotProps.data.title }} </template>
      </Column>
    </DataTable>
        {{ selectedQuestionnaire }}

  </div>

  <div class="flex justify-end gap-2">
    <Button label="Save" @click="emits('select', selectedQuestionnaire)" />
  </div>
</template>
