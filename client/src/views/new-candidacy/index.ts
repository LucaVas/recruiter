import type { NewCandidacyDto } from '@/stores/candidacy/types';
import type { RawCandidateDto } from '@/stores/candidate/types';

export function formToNewCandidacy(
  candidacyDetails: any,
  candidateSelectedId: number
): NewCandidacyDto {
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

export type CandidateForm = Omit<RawCandidateDto, 'totalExperience' | 'currentCtc'> & {
  totalExperience: string;
  currentCtc: string;
};
