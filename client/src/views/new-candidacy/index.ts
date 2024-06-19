import type { NewCandidacy } from '@/stores/candidacy/schema';
import type { Candidate } from '@/stores/candidate/schema';
import type { Job } from '@/stores/job/schema';
import { ref } from 'vue';

export const headerModalOpen = ref(false);
export const job = ref<Job>();

// candidacy submission
export const candidacySubmitted = ref(false);
export const submittingNewCandidacy = ref(false);
// candidate
export const searchingForCandidate = ref(false);
export const selectedCandidate = ref<Candidate | null>();
export const searchedCandidate = ref<Candidate>();
export const creatingCandidate = ref(false);
export const candidateCreated = ref(false);

export function formToNewCandidacy(
  candidacyDetails: any,
  candidateSelectedId: number
): NewCandidacy {
  return {
    ...candidacyDetails,
    jobId: Number(candidacyDetails.jobId),
    candidateId: Number(candidateSelectedId),
    relevantExperience: Number(candidacyDetails.relevantExperience),
    expectedCtc: Number(candidacyDetails.expectedCtc),
    officialNoticePeriod: Number(candidacyDetails.officialNoticePeriod),
    actualNoticePeriod: Number(candidacyDetails.actualNoticePeriod),
  };
}
