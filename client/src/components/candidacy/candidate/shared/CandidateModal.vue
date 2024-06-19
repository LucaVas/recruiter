<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref } from 'vue';
import type { Candidate, NewCandidate } from '@/stores/candidate/schema';
import { createCandidate } from '@/stores/candidate';
import { showError, showSuccess } from '@/utils/errorUtils';
import { ApiError } from '@/utils/types';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import { useToast } from 'primevue/usetoast';

const props = defineProps<{
  candidate: Candidate | null;
  visible: boolean;
  isUpdate: boolean;
}>();

const toast = useToast();
const creatingCandidate = ref(false);

async function create(candidate: NewCandidate) {
  creatingCandidate.value = true;
  try {
    const res = await createCandidate(candidate);
    showSuccess(toast, 'Candidate created successfully');
    emits('save', res.candidate);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    creatingCandidate.value = false;
  }
}

const emits = defineEmits<{
  (e: 'close'): void;
  (e: 'save', candidate: Candidate): void;
}>();

const emptyCandidate = ref<NewCandidate>({
  pan: '',
  name: '',
  phone: '',
  email: '',
  totalExperience: 0,
  education: '',
  currentCtc: 0,
});
const tmpCandidate = ref(emptyCandidate.value);
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      v-if="tmpCandidate"
      :visible="props.visible"
      @show="
        props.isUpdate && props.candidate
          ? (tmpCandidate = props.candidate)
          : (tmpCandidate = {
              ...emptyCandidate,
            })
      "
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
          <InputText placeholder="Name" autocomplete="off" v-model="tmpCandidate.name" />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-phone"></i>
          </InputGroupAddon>
          <InputMask
            id="phone"
            v-model="tmpCandidate.phone"
            mask="(999) 999-9999"
            placeholder="Phone"
            :unmask="true"
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
            v-model="tmpCandidate.email"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-id-card"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Pan"
            autocomplete="off"
            v-model="tmpCandidate.pan"
            :disabled="isUpdate"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-book"></i>
          </InputGroupAddon>
          <InputText placeholder="Education" autocomplete="off" v-model="tmpCandidate.education" />
        </InputGroup>

        <div class="flex flex-col gap-2">
          <label class="text-sm">Total Experience & Current CTC</label>

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
                v-model="tmpCandidate.totalExperience"
              />
              <InputGroupAddon>Years</InputGroupAddon>
            </InputGroup>

            <InputGroup>
              <InputGroupAddon>
                <i class="pi pi-wallet"></i>
              </InputGroupAddon>
              <InputNumber
                placeholder="Current CTC"
                autocomplete="off"
                :min="0"
                v-model="tmpCandidate.currentCtc"
              />
              <InputGroupAddon>INR</InputGroupAddon>
            </InputGroup>
          </div>
        </div>
      </div>

      <div class="flex justify-end gap-2">
        <Button label="Cancel" severity="secondary" @click="$emit('close')" />
        <Button label="Save" @click="create(tmpCandidate)" />
      </div>
    </Dialog>
  </div>
</template>
