<script setup lang="ts">
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { getCandidacy } from '@/stores/candidacy/index';
import { useRoute, useRouter } from 'vue-router';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import { ApiError } from '@/utils/types';
import type { CandidacyDto } from '@/stores/candidacy/schema';
import ProgressSpinner from 'primevue/progressspinner';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();
const router = useRouter();
const headerModalOpen = ref(false);

// candidacy details
const candidacy = ref<CandidacyDto>();

onMounted(async () => {
  const route = useRoute();
  const id = Number(route.params.id);
  try {
    const res = await getCandidacy(id);
    candidacy.value = res;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
});
</script>
<template>
  <div v-if="!candidacy" class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>

  <div v-else class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <div v-if="candidacy.job">
        <CandidacyHeader
          :status="candidacy.job.status"
          :client="candidacy.job.client.name"
          :name="candidacy.job.name"
          @openModal="headerModalOpen = true"
        />
        <CandidacyHiringDetailsModal
          :visible="headerModalOpen"
          @close="headerModalOpen = false"
          :job="candidacy.job"
        />
      </div>

      <CandidateTable :candidateToDisplay="candidacy.candidate" @selectCandidate="null" />

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
                v-model="candidacy.relevantExperience"
                disabled
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
                v-model="candidacy.expectedCtc"
                placeholder="Expected CTC"
                disabled
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
              <InputNumber
                id="officialNoticePeriod"
                v-model="candidacy.officialNoticePeriod"
                disabled
              />
              <InputGroupAddon> days </InputGroupAddon>
            </InputGroup>
          </div>

          <div class="flex w-full flex-col gap-2">
            <label class="text-sm" for="actualNoticePeriod"
              >Actual Notice Period (if different)</label
            >
            <InputGroup>
              <InputGroupAddon>
                <i class="pi pi-calendar" />
              </InputGroupAddon>
              <InputNumber
                id="actualNoticePeriod"
                v-model="candidacy.actualNoticePeriod"
                disabled
              />
              <InputGroupAddon> days </InputGroupAddon>
            </InputGroup>
          </div>
        </div>

        <div class="flex w-full flex-col gap-2">
          <label class="text-sm" for="wantedCvs">Quick Join Remarks</label>
          <div class="field p-fluid flex w-full">
            <Textarea
              v-model="candidacy.reasonForQuickJoin"
              class="w-full"
              rows="4"
              cols="30"
              placeholder="Reason for quick join"
              disabled
            />
          </div>
        </div>
      </div>
    </div>

    <div class="flex w-full justify-between">
      <Button
        label="Back to candidacies"
        size="small"
        @click="
          {
            $emit('back');
            router.push({ name: 'Candidacies' });
          }
        "
      />
      <div>
        <Button
          v-if="
            candidacy.status !== 'WITHDRAWN' &&
            candidacy.status !== 'ARCHIVED' &&
            (candidacy.status === 'SENT_TO_CLIENT' || candidacy.status === 'REJECTED')
          "
          label="Accept"
          size="small"
          @click="console.log('approved')"
        />
        <Button
          v-if="
            candidacy.status !== 'WITHDRAWN' &&
            candidacy.status !== 'ARCHIVED' &&
            (candidacy.status === 'SENT_TO_CLIENT' || candidacy.status === 'ACCEPTED')
          "
          label="Reject"
          size="small"
          @click="console.log('rejected')"
        />
        <Button
          v-if="candidacy.status !== 'ARCHIVED'"
          label="Archive"
          size="small"
          @click="console.log('rejected')"
        />
      </div>
    </div>
  </div>
</template>
