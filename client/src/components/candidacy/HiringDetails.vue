<script setup lang="ts">
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import { ref } from 'vue';

const { isArchived } = defineProps<{
  isArchived: boolean;
}>();

const details = ref({
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
});

const emit = defineEmits<{
  (e: 'input', content: typeof details.value): void;
}>();
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
          <InputNumber
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
          <InputNumber
            id="expectedCtc"
            v-model="details.expectedCtc"
            @input="emit('input', details)"
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
        <label class="text-sm" for="officialNoticePeriod">Official Notice Period</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputNumber
            id="officialNoticePeriod"
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
        <label class="text-sm" for="actualNoticePeriod">Actual Notice Period (Optional)</label>
        <InputGroup>
          <InputGroupAddon>
            <i class="pi pi-calendar" />
          </InputGroupAddon>
          <InputNumber
            id="actualNoticePeriod"
            v-model="details.actualNoticePeriod"
            @input="emit('input', details)"
            required
            :min="0"
            :disabled="isArchived"
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
        />
      </div>
    </div>
  </div>
</template>
