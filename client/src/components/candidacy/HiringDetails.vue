<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { ref, onMounted } from 'vue';
import type { CandidacyForm } from '@/views/candidacy/mappers';

const { candidacy, isArchived } = defineProps<{
  candidacy: CandidacyForm;
  isArchived: boolean;
}>();

const details = ref({
  relevantExperience: '',
  expectedCtc: '',
  officialNoticePeriod: '',
  actualNoticePeriod: '',
  reasonForQuickJoin: '',
});

const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();

onMounted(() => {
  details.value.relevantExperience = candidacy.relevantExperience;
  details.value.expectedCtc = candidacy.expectedCtc;
  details.value.officialNoticePeriod = candidacy.officialNoticePeriod;
  details.value.actualNoticePeriod = candidacy.actualNoticePeriod;
  details.value.reasonForQuickJoin = candidacy.reasonForQuickJoin;
});
</script>

<template>
  <div class="flex flex-col gap-8">
    <div class="flex w-full flex-col gap-6 md:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="wantedCvs">Relevant Work Experience</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputText
            type="number"
            id="relevantExperience"
            v-model="details.relevantExperience"
            @input="emit('input', details)"
            required
            :min="0"
            :max="45"
            :disabled="isArchived"
          />
          <InputGroupAddon> years </InputGroupAddon>
        </InputGroup>
      </div>

      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="expectedCtc">Expected CTC</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-money-bill" />
          </InputGroupAddon>
          <InputText
            id="expectedCtc"
            type="number"
            v-model="details.expectedCtc"
            @input="emit('input', details)"
            placeholder="Expected CTC"
            :min="0"
            required
            :disabled="isArchived"
          />
          <InputGroupAddon> INR </InputGroupAddon>
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-6 md:flex-row">
      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="officialNoticePeriod">Official Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputText
            id="officialNoticePeriod"
            type="number"
            v-model="details.officialNoticePeriod"
            @input="emit('input', details)"
            required
            :min="0"
            :disabled="isArchived"
          />
          <InputGroupAddon> days </InputGroupAddon>
        </InputGroup>
      </div>

      <div class="flex w-full flex-col gap-2">
        <label class="text-sm" for="actualNoticePeriod">Actual Notice Period (if different)</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputText
            id="actualNoticePeriod"
            v-model="details.actualNoticePeriod"
            @input="emit('input', details)"
            :disabled="isArchived"
            type="number"
            :min="0"
          />
          <InputGroupAddon> days </InputGroupAddon>
        </InputGroup>
      </div>
    </div>

    <div class="flex w-full flex-col gap-2">
      <label class="text-sm" for="wantedCvs">Quick Join Remarks</label>
      <div class="field p-fluid flex w-full">
        <Textarea
          v-model="details.reasonForQuickJoin"
          @input="emit('input', details)"
          class="w-full"
          rows="4"
          cols="30"
          placeholder="Reason for quick join"
          :disabled="details.actualNoticePeriod === ''"
        />
      </div>
    </div>
  </div>
</template>
