<script setup lang="ts">
import CandidacyHiringDetailsModal from '@/components/candidacy/header/CandidacyHiringDetailsModal.vue';
import { useToast } from 'primevue/usetoast';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';
import { onMounted, ref } from 'vue';
import { getJob } from '@/stores/job';
import { useRoute, useRouter } from 'vue-router';
import { findCandidate } from '@/stores/candidate';
import { submitCandidacy } from '@/stores/candidacy';
import { ApiError } from '@/utils/types';
import FilesUploader from '@/components/uploader/FilesUploader.vue';
import CandidacyHeader from '@/components/candidacy/CandidacyHeader.vue';
import CandidacyFooter from '@/components/candidacy/CandidacyFooter.vue';
import type { Candidate } from '@/stores/candidate/schema';
import Success from '@/components/Success.vue';
import SearchCandidate from '@/components/candidacy/candidate/SearchCandidate.vue';
import CandidateTable from '@/components/candidacy/candidate/CandidateTable.vue';
import CandidateModal from '@/components/candidacy/candidate/shared/CandidateModal.vue';
import { showError } from '@/utils/errorUtils';
import { DEFAULT_SERVER_ERROR } from '@/consts';
import type { NewCandidacy } from '@/stores/candidacy/schema';
import type { Job } from '@/stores/job/schema';

const jobId = ref<number>();
const toast = useToast();
const router = useRouter();
const loading = ref(false);

const newCandidateModalOpen = ref(false);
const headerModalOpen = ref(false);

const searching = ref(false);
const searchedCandidate = ref<Candidate>();

const submittingNewCandidacy = ref(false);
const candidacySubmitted = ref(false);

const candidacyFiles = ref<File[]>([]);
const job = ref<Job>();
const candidacy = ref<NewCandidacy>({
  candidate: {} as Candidate,
  job: {} as Job,
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
  recruiterComment: '',
});

async function submit(candidacy: NewCandidacy) {
  submittingNewCandidacy.value = true;
  if (!job.value) return;
  try {
    candidacy.job = job.value;
    await submitCandidacy(candidacy, candidacyFiles.value);
    candidacySubmitted.value = true;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    submittingNewCandidacy.value = false;
  }
}
async function searchCandidate(identifier: string) {
  searching.value = true;
  try {
    const res = await findCandidate(identifier);
    searchedCandidate.value = res.candidate;
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    searching.value = false;
  }
}

const loadJob = async () => {
  if (!jobId.value) return;
  loading.value = true;
  try {
    job.value = await getJob(jobId.value);
  } catch (err) {
    if (err instanceof ApiError) showError(toast, err.message, err.title);
    else if (err instanceof Error) showError(toast, err.message);
    else showError(toast, DEFAULT_SERVER_ERROR);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  const route = useRoute();
  jobId.value = Number(route.params.id);
  await loadJob();
});
</script>

<template>
  <div class="flex w-full flex-col gap-8 pb-6">
    <div class="flex h-full w-full flex-col gap-6">
      <div v-if="job">
        <CandidacyHeader :candidacy="candidacy" :job="job" @openModal="headerModalOpen = true" />
        <CandidacyHiringDetailsModal
          :visible="headerModalOpen"
          @close="headerModalOpen = false"
          :job="candidacy.job"
        />
      </div>

      <div class="flex w-full flex-col gap-2">
        <label class="text-sm">Search for a candidate</label>
        <CandidateModal
          :isUpdate="false"
          :candidate="candidacy.candidate"
          :visible="newCandidateModalOpen"
          @close="newCandidateModalOpen = false"
          @save="
            (candidate: Candidate) => {
              candidacy.candidate = candidate;
              newCandidateModalOpen = false;
            }
          "
        />

        <SearchCandidate
          @search="(identifier: string) => searchCandidate(identifier)"
          @openNewCandidateModal="newCandidateModalOpen = true"
          :searching="searching"
        />

        <CandidateTable
          v-if="searchedCandidate"
          :candidateToDisplay="searchedCandidate"
          @selectCandidate="(candidate) => (candidacy.candidate = candidate)"
        />
      </div>

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

      <div class="flex flex-col gap-8">
        <div class="flex w-full flex-col gap-2">
          <label class="text-sm" for="wantedCvs">Comments</label>
          <div class="flex w-full">
            <Textarea
              v-model="candidacy.recruiterComment"
              class="w-full"
              rows="4"
              cols="30"
              placeholder="Your comments (optional)"
            />
          </div>
        </div>
      </div>
      <FilesUploader
        @addFiles="
          (files: File[]) => {
            candidacyFiles = files;
          }
        "
      />
    </div>

    <Success
      :visible="candidacySubmitted"
      title="Candidacy submitted"
      message="Candidacy submitted successfully!"
      @close="
        {
          candidacySubmitted = false;
          router.push({ name: 'CandidaciesPage' });
        }
      "
    />

    <CandidacyFooter
      v-if="job"
      :disabled="job.status === 'ARCHIVED'"
      :candidacySubmitted="candidacySubmitted"
      :submittingCandidacy="submittingNewCandidacy"
      :isUpdate="false"
      @submit="submit(candidacy)"
      @back="candidacySubmitted = false"
    />
  </div>
</template>
