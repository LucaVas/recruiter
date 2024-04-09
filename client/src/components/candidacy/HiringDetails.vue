<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import type { Currency } from '../../stores/job/types';
import { ref } from 'vue';

const { isArchived } = defineProps<{
  isArchived: boolean;
}>();

const candidacyDetails = ref({
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
  // remarks: '',
  // comments: '',
});
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex w-full flex-col gap-6 md:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label for="wantedCvs">Relevant Work Experience</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputNumber
            id="relevantExperience"
            v-model="candidacyDetails.relevantExperience"
            placeholder="Relevant work experience"
            required
            :min="0"
            :max="45"
            :disabled="isArchived"
          />
          <InputGroupAddon> years </InputGroupAddon>
        </InputGroup>
      </div>

      <div class="flex w-full flex-col gap-2">
        <label for="expectedCtc">Expected CTC</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <InputNumber
            id="expectedCtc"
            v-model="candidacyDetails.expectedCtc"
            placeholder="Expected CTC"
            :min="0"
            required
            :disabled="isArchived"
          />
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-6 md:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label for="officialNoticePeriod">Official Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputNumber
            id="officialNoticePeriod"
            v-model="candidacyDetails.officialNoticePeriod"
            required
            :min="0"
            :disabled="isArchived"
          />
          <InputGroupAddon> days </InputGroupAddon>
        </InputGroup>
      </div>

      <div class="flex w-full flex-col gap-2">
        <label for="actualNoticePeriod">Actual Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputNumber
            id="actualNoticePeriod"
            v-model="candidacyDetails.actualNoticePeriod"
            required
            :min="0"
            :disabled="isArchived"
          />
          <InputGroupAddon> days </InputGroupAddon>
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label for="wantedCvs">Quick Join Remarks</label>
      <div class="field p-fluid flex w-full">
        <Textarea
          v-model="candidacyDetails.reasonForQuickJoin"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Reason for quick join"
        />
      </div>
    </div>
  </div>
</template>
