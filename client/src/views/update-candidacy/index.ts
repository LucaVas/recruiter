import type { RawCandidacy } from '@/stores/candidacy/schema';
import type { Candidate } from '@/stores/candidate/schema';
import type { Job } from '@/stores/job/schema';
import { ref } from 'vue';

export const headerModalOpen = ref(false);
export const job = ref<Job>();

// candidacy details
export const candidacy = ref<RawCandidacy>();
export const jobId = ref<number>();
export const pan = ref<string>();

// candidacy submission
export const candidacyUpdated = ref(false);
export const updatingCandidacy = ref(false);
export const candidate = ref<Candidate>();
