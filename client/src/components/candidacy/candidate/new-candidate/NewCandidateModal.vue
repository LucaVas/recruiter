<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref } from 'vue';
import type { NewCandidateDto } from '@/stores/candidate/types';
import { ApiError } from '@/utils/types';
import { invalidFields, invalidCandidate } from '.';
import type { CandidateDto } from '@/stores/candidate/types';
import { addCandidate } from '@/stores/candidate/';

const creatingCandidate = ref(false);
const newCandidateError = ref('');
const visible = ref(true);
export type CandidateDetails = typeof candidateDetails.value;
const candidateDetails = ref({
  name: '',
  phone: '',
  email: '',
  pan: '',
  education: '',
  totalExperience: '',
  currentCtc: '',
});
function toNewCandidate(): NewCandidateDto {
  return {
    ...candidateDetails.value,
    totalExperience: Number(candidateDetails.value.totalExperience),
    currentCtc: Number(candidateDetails.value.currentCtc),
  };
}
async function createCandidate() {
  creatingCandidate.value = true;
  if (invalidCandidate(candidateDetails.value, newCandidateError)) return;
  try {
    const payload = toNewCandidate();
    console.log('Sending:', payload);
    const res = await addCandidate(toNewCandidate());
    creatingCandidate.value = false;
    emits('selectCandidate', res.candidate);
  } catch (err) {
    if (err instanceof ApiError) newCandidateError.value = err.message;
  } finally {
    creatingCandidate.value = false;
  }
}

const emits = defineEmits<{
  (e: 'selectCandidate', selectedCandidate: CandidateDto): void;
  (e: 'close'): void;
}>();
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      v-model:visible="visible"
      @hide="$emit('close')"
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
            v-model="candidateDetails.name"
            :invalid="invalidFields.name.invalid"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-phone"></i>
          </InputGroupAddon>
          <InputMask
            id="phone"
            v-model="candidateDetails.phone"
            mask="(999) 999-9999"
            placeholder="Phone"
            :unmask="true"
            :invalid="invalidFields.phone.invalid"
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
            v-model="candidateDetails.email"
            :invalid="invalidFields.email.invalid"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-id-card"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Pan"
            autocomplete="off"
            v-model="candidateDetails.pan"
            :invalid="invalidFields.pan.invalid"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-book"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Education"
            autocomplete="off"
            v-model="candidateDetails.education"
            :invalid="invalidFields.education.invalid"
          />
        </InputGroup>

        <div class="mb-5 flex flex-col items-center justify-between gap-3 md:flex-row">
          <InputGroup>
            <InputGroupAddon>
              <i class="pi pi-calendar"></i>
            </InputGroupAddon>
            <InputText
              placeholder="Total Experience"
              autocomplete="off"
              type="number"
              min="0"
              v-model="candidateDetails.totalExperience"
              :invalid="invalidFields.totalExperience.invalid"
            />
          </InputGroup>
          <InputGroup>
            <InputGroupAddon>
              <i class="pi pi-wallet"></i>
            </InputGroupAddon>
            <InputText
              placeholder="Current CTC"
              autocomplete="off"
              type="number"
              min="0"
              v-model="candidateDetails.currentCtc"
              :invalid="invalidFields.currentCtc.invalid"
            />
            <InputGroupAddon>INR</InputGroupAddon>
          </InputGroup>
        </div>
      </div>

      <Message v-if="newCandidateError" severity="error" :closable="false" class="w-full">{{
        newCandidateError
      }}</Message>

      <div class="justify-content-end flex gap-2">
        <Button type="button" label="Cancel" severity="secondary" @click="$emit('close')"></Button>
        <Button type="button" label="Save" @click="createCandidate()"></Button>
      </div>
    </Dialog>
  </div>
</template>
../../../stores/candidate/types
