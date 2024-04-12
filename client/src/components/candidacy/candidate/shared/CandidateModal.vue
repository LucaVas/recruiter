<script setup lang="ts">
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref, onMounted } from 'vue';
import { invalidFields } from './index';
import type { CandidateForm } from '@/views/new-candidacy/index';
import { generateEmptyCandidateForm } from '@/views/new-candidacy/utils';

const details = ref<CandidateForm>();

const { candidate, visible } = defineProps<{
  candidate: CandidateForm;
  visible: boolean;
}>();

const emits = defineEmits<{
  (e: 'update', content: CandidateForm): void;
  (e: 'close'): void;
  (e: 'save'): void;
}>();

onMounted(() => {
  details.value = candidate;
});
</script>

<template>
  <div class="card flex justify-center">
    <Dialog
      v-if="details"
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
            v-model="details.name"
            :invalid="invalidFields.name.invalid"
            @input="emits('update', details)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-phone"></i>
          </InputGroupAddon>
          <InputMask
            id="phone"
            v-model="details.phone"
            mask="(999) 999-9999"
            placeholder="Phone"
            :unmask="true"
            :invalid="invalidFields.phone.invalid"
            @input="emits('update', details)"
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
            v-model="details.email"
            :invalid="invalidFields.email.invalid"
            @input="emits('update', details)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-id-card"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Pan"
            autocomplete="off"
            v-model="details.pan"
            :invalid="invalidFields.pan.invalid"
            @input="emits('update', details)"
          />
        </InputGroup>

        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-book"></i>
          </InputGroupAddon>
          <InputText
            placeholder="Education"
            autocomplete="off"
            v-model="details.education"
            :invalid="invalidFields.education.invalid"
            @input="emits('update', details)"
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
              v-model="details.totalExperience"
              :invalid="invalidFields.totalExperience.invalid"
              @input="emits('update', details)"
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
              v-model="details.currentCtc"
              :invalid="invalidFields.currentCtc.invalid"
              @input="emits('update', details)"
            />
            <InputGroupAddon>INR</InputGroupAddon>
          </InputGroup>
        </div>
      </div>

      <div class="flex justify-end gap-2">
        <Button
          label="Cancel"
          severity="secondary"
          @click="
            $emit('close');
            details = generateEmptyCandidateForm();
          "
        />
        <Button
          label="Save"
          @click="
            emits('save');
            details = generateEmptyCandidateForm();
          "
        />
      </div>
    </Dialog>
  </div>
</template>
@/views/new-candidacy/index@/views/new-candidacy/utils
