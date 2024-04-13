<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref } from 'vue';
import { invalidFields } from './index';
import type { RawCandidateDto } from '@/stores/candidate/types';

const { candidate, visible, isUpdate } = defineProps<{
  candidate: RawCandidateDto;
  visible: boolean;
  isUpdate: boolean
}>();

const emits = defineEmits<{
  (e: 'update', content: RawCandidateDto): void;
  (e: 'close'): void;
  (e: 'save'): void;
}>();

const candidateForm = ref(candidate)
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      v-if="candidateForm"
      :visible="visible"
      @update:visible="$emit('close')"
      closeOnEscape
      modal
      header="New Candidate"
      class="max-w-[60rem]"
    >
      <div class="flex flex-col gap-2">
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-user"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Name"
            autocomplete="off"
            v-model="candidateForm.name"
            :invalid="invalidFields.name.invalid"
            @input="emits('update', candidateForm)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-phone"></i>
          </InputGroupAddon>
          <InputMask
            id="phone"
            v-model="candidateForm.phone"
            mask="(999) 999-9999"
            placeholder="Phone"
            :unmask="true"
            :invalid="invalidFields.phone.invalid"
            @input="emits('update', candidateForm)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-at"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Email"
            autocomplete="off"
            type="email"
            v-model="candidateForm.email"
            :invalid="invalidFields.email.invalid"
            @input="emits('update', candidateForm)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-id-card"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Pan"
            autocomplete="off"
            v-model="candidateForm.pan"
            :invalid="invalidFields.pan.invalid"
            @input="isUpdate ? null : emits('update', candidateForm)"
            :disabled="isUpdate"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-book"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Education"
            autocomplete="off"
            v-model="candidateForm.education"
            :invalid="invalidFields.education.invalid"
            @input="emits('update', candidateForm)"
          />
        </InputGroup>

        <div class="mb-5 flex flex-col items-center justify-between gap-3 md:flex-row">
          <InputGroup>
            <InputGroupAddon>
              <i class="pi pi-calendar"></i>
            </InputGroupAddon>
            <InputNumber
              placeholder="Total Experience"
              autocomplete="off"
              :min="0"
              :max="50"
              v-model="candidateForm.totalExperience"
              :invalid="invalidFields.totalExperience.invalid"
              @input="emits('update', candidateForm)"
            />
          </InputGroup>
          <InputGroup>
            <InputGroupAddon>
              <i class="pi pi-wallet"></i>
            </InputGroupAddon>
            <InputNumber
              placeholder="Current CTC"
              autocomplete="off"
              :min="0"
              v-model="candidateForm.currentCtc"
              :invalid="invalidFields.currentCtc.invalid"
              @input="emits('update', candidateForm)"
            />
            <InputGroupAddon>INR</InputGroupAddon>
          </InputGroup>
        </div>
      </div>

      <div class="flex justify-end gap-2">
        <Button label="Cancel" severity="secondary" @click="$emit('close')" />
        <Button label="Save" @click="emits('save')" />
      </div>
    </Dialog>
  </div>
</template>
