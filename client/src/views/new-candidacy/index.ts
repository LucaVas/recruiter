import type { NewCandidacyRequest } from '@/stores/candidacy/types';

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
