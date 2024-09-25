<script setup lang="ts">
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidacyHiringDetailsModal from '@/components/modals/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import { getCandidacy, updateCandidacy } from '@/stores/candidacy/index';
import { useRoute, useRouter } from 'vue-router';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import Success from '@/components/Success.vue';
import { ApiError } from '@/utils/types';
import type { CandidacyDto, UpdateCandidacy } from '@/stores/candidacy/schema';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import ProgressSpinner from 'primevue/progressspinner';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';

const toast = useToast();
const router = useRouter();
const headerModalOpen = ref(false);

// candidacy details
const candidacy = ref<CandidacyDto>();

// candidacy submission
const candidacyUpdated = ref(false);
const updatingCandidacy = ref(false);

async function update(candidacy: UpdateCandidacy | undefined) {
  if (!candidacy) return;
  updatingCandidacy.value = true;
  try {
    await updateCandidacy(candidacy.id, candidacy);
    candidacyUpdated.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    updatingCandidacy.value = false;
  }
}

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
  <Success
    :visible="candidacyUpdated"
    title="Candidacy Updated"
    message="Candidacy updated successfully!"
    @close="
      {
        candidacyUpdated = false;
        router.push({ name: 'CandidaciesPage' });
      }
    "
  />

  <div v-if="!candidacy" class="flex w-full items-center justify-center">
    <ProgressSpinner />
  </div>

  <div v-else class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <div v-if="candidacy.job">
        <CandidacyHeader :candidacy="candidacy" @openModal="headerModalOpen = true" />
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
                required
                :min="0"
                :max="45"
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
                :min="0"
                required
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
                required
                :min="0"
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
              v-model="candidacy.reasonForQuickJoin"
              class="w-full"
              rows="4"
              cols="30"
              placeholder="Reason for quick join"
              :disabled="candidacy.actualNoticePeriod >= candidacy.officialNoticePeriod"
            />
          </div>
        </div>
      </div>
    </div>

    <CandidacyFooter
      :disabled="candidacy.job.status === 'DELETED'"
      :candidacySubmitted="candidacyUpdated"
      :submittingCandidacy="updatingCandidacy"
      :isUpdate="true"
      @update="update(candidacy)"
    />
  </div>
</template>
