import type { NewCandidacyRequest, RawCandidacy } from '@/stores/candidacy/schema';
import type { Candidate, NewCandidateRequest } from '@/stores/candidate/schema';
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

export const candidacy = ref<RawCandidacy>({
  relevantExperience: 0,
  expectedCtc: 0,
  officialNoticePeriod: 0,
  actualNoticePeriod: 0,
  reasonForQuickJoin: '',
  recruiterComment: '',
});

export const candidate = ref<NewCandidateRequest>({
  name: '',
  phone: '',
  email: '',
  pan: '',
  totalExperience: 0,
  education: '',
  currentCtc: 0,
});

export function formToNewCandidacy(
  candidacyDetails: any,
  candidateSelectedId: number
): NewCandidacyRequest {
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
