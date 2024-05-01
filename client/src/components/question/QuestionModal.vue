<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref, onMounted } from 'vue';
import type { Question, QuestionForm } from '@/stores/question/schema';
import { getAllSkills } from '@/stores/skill';
import { getAllClients } from '@/stores/client';

const loading = ref(false);

const { question, visible, isUpdate } = defineProps<{
  question?: Question | undefined;
  visible: boolean;
  isUpdate: boolean;
}>();

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'save', content: QuestionForm): void;
}>();

const questionForm = ref<QuestionForm>({
  title: '',
  text: '',
  answer: '',
  division: '',
  clientId: 0,
  skillId: undefined,
});

const clients = ref<{ name: string; label: number }[]>();
const skills = ref<{ name: string; label: number }[]>();

onMounted(async () => {
  loading.value = true;
  try {
    clients.value = (await getAllClients()).map((c) => ({
      name: c.name,
      label: c.id,
    }));
    skills.value = (await getAllSkills()).map((s) => ({
      name: s.name,
      label: s.id,
    }));
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="flex justify-center">
    <Dialog
      v-if="questionForm"
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="New Question"
      class="w-[90%] sm:w-2/3 md:w-2/3 lg:w-1/3"
    >
      <div class="mb-5 flex flex-col gap-2">
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
            :options="clients"
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
          <Dropdown
            v-model="questionForm.skillId"
            :options="skills"
            optionLabel="name"
            optionValue="label"
            placeholder="Select a skill"
            class="md:w-14rem w-full"
          />
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
      </div>

      <div class="flex justify-end gap-2">
        <Button label="Cancel" severity="secondary" @click="$emit('close')" />
        <Button label="Save" @click="emits('save', questionForm)" />
      </div>
    </Dialog>
  </div>
</template>
