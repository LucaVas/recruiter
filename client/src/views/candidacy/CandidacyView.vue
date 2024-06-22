<script setup lang="ts">
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import { onMounted, ref } from 'vue';
import {
  acceptCandidacy,
  getCandidacy,
  rejectCandidacy,
  archiveCandidacy,
  reopenCandidacy,
} from '@/stores/candidacy/index';
import { useRoute, useRouter } from 'vue-router';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import { ApiError } from '@/utils/types';
import type { CandidacyDto } from '@/stores/candidacy/schema';
import ProgressSpinner from 'primevue/progressspinner';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';
import { showError, showSuccess } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';

const toast = useToast();
const router = useRouter();
const headerModalOpen = ref(false);
const rejecting = ref(false);
const accepting = ref(false);
const archiving = ref(false);
const reopening = ref(false);

// candidacy details
const candidacy = ref<CandidacyDto>();
const candidacyId = ref<number>();

const reject = async (id: number) => {
  rejecting.value = true;
  try {
    await rejectCandidacy(id);
    showSuccess(toast, 'Candidacy rejected successfully');
    await init(candidacyId.value);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    rejecting.value = false;
  }
};

const accept = async (id: number) => {
  accepting.value = true;
  try {
    await acceptCandidacy(id);
    showSuccess(toast, 'Candidacy accepted successfully');
    await init(candidacyId.value);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    accepting.value = false;
  }
};

const archive = async (id: number) => {
  archiving.value = true;
  try {
    await archiveCandidacy(id);
    showSuccess(toast, 'Candidacy archived successfully');
    await init(candidacyId.value);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    archiving.value = false;
  }
};

const reopen = async (id: number) => {
  reopening.value = true;
  try {
    await reopenCandidacy(id);
    showSuccess(toast, 'Candidacy reopened successfully');
    await init(candidacyId.value);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    reopening.value = false;
  }
};

const init = async (id: number | undefined) => {
  if (!id) return;

  try {
    const res = await getCandidacy(id);
    candidacy.value = res;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  }
};

onMounted(async () => {
  const route = useRoute();
  candidacyId.value = Number(route.params.id);
  await init(candidacyId.value);
});
</script>
<template>
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
        class="hidden md:flex"
        label="Back to candidacies"
        size="small"
        outlined
        @click="
          {
            $emit('back');
            router.push({ name: 'Candidacies' });
          }
        "
      />
      <Button
        class="md:hidden"
        size="small"
        outlined
        icon="pi pi-arrow-left"
        @click="
          {
            $emit('back');
            router.push({ name: 'Candidacies' });
          }
        "
      />
      <div class="flex gap-2">
        <Button
          v-if="
            candidacy.status !== 'WITHDRAWN' &&
            candidacy.status !== 'ARCHIVED' &&
            (candidacy.status === 'SENT_TO_CLIENT' || candidacy.status === 'REJECTED')
          "
          label="Accept"
          size="small"
          outlined
          severity="success"
          @click="accept(candidacy.id)"
        />
        <Button
          v-if="
            candidacy.status !== 'WITHDRAWN' &&
            candidacy.status !== 'ARCHIVED' &&
            (candidacy.status === 'SENT_TO_CLIENT' || candidacy.status === 'ACCEPTED')
          "
          label="Reject"
          size="small"
          outlined
          severity="danger"
          @click="reject(candidacy.id)"
        />
        <Button
          v-if="candidacy.status !== 'ARCHIVED'"
          label="Archive"
          size="small"
          outlined
          severity="secondary"
          @click="archive(candidacy.id)"
        />
        <Button
          v-if="candidacy.status === 'ARCHIVED'"
          label="Reopen"
          size="small"
          outlined
          severity="secondary"
          @click="reopen(candidacy.id)"
        />
      </div>
    </div>
  </div>
</template>
