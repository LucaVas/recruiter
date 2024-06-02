<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Chips from 'primevue/chips';
import { ref } from 'vue';
import { type NewQuestionnaire, type Questionnaire } from '@/stores/questionnaire/schema';
import { getQuestionnairesByClient } from '@/stores/questionnaire/api';
import { useToast } from 'primevue/usetoast';
import { handleError } from '@/utils/errorUtils';

const toast = useToast();
const { questionnaire, visible } = defineProps<{
  questionnaire: Questionnaire;
  visible: boolean;
}>();

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'save', content: Questionnaire): void;
}>();

const questionnaires = ref<Questionnaire[]>([]);
const tmpQuestionnaire = ref<NewQuestionnaire>({
  title: '',
  clientName: '',
  questions: [],
});

const getQuestionnaires = async (clientName: string) => {
  try {
    questionnaires.value = await getQuestionnairesByClient(clientName);
  } catch (err) {
    handleError(toast, err);
  }
};

const mappedClients = ref<{ name: string; label: number }[]>();
</script>

<template>
  <Dialog
    :visible="visible"
    @show="getQuestionnaires('IBM')"
    @update:visible="$emit('close')"
    closeOnEscape
    modal
    header="New Question"
    class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
  >
    {{ questionnaires }}
    <!-- <div class="mb-5 flex flex-col gap-2">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-id-card"></i>
        </InputGroupAddon>
        <InputText placeholder="Title" autocomplete="off" v-model="questionForm.title" />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-building"></i>
        </InputGroupAddon>
        <Dropdown
          v-model="questionForm.clientId"
          :options="mappedClients"
          optionLabel="name"
          optionValue="label"
          placeholder="Select a client"
          class="md:w-14rem w-full"
        />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-briefcase"></i>
        </InputGroupAddon>
        <InputText placeholder="Division" autocomplete="off" v-model="questionForm.division" />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-wrench"></i>
        </InputGroupAddon>
        <div class="p-fluid w-full overflow-x-auto">
          <Chips
            v-model="questionForm.skillNames"
            separator=","
            :allow-duplicate="false"
            class="w-full"
            :max="5"
            :placeholder="questionForm.skillNames.length === 0 ? 'Skills' : ''"
          />
        </div>
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-briefcase"></i>
        </InputGroupAddon>
        <InputText
          placeholder="Write your question here"
          autocomplete="off"
          v-model="questionForm.text"
        />
      </InputGroup>

      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-briefcase"></i>
        </InputGroupAddon>
        <InputText
          placeholder="Write the question's answer here"
          autocomplete="off"
          v-model="questionForm.answer"
        />
      </InputGroup>
    </div> -->

    <div class="flex justify-end gap-2">
      <Button label="Cancel" severity="secondary" @click="$emit('close')" />
      <Button label="Save" @click="emits('save', tmpQuestionnaire)" />
    </div>
  </Dialog>
</template>
