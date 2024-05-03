import type { Candidacy, UpdateCandidacyRequest } from '@/stores/candidacy/schema';

export function mapCandidacyToForm(candidacy: Candidacy): CandidacyForm {
  return {
    ...candidacy,
    jobId: candidacy.job.id,
    relevantExperience: candidacy.relevantExperience.toString(),
    expectedCtc: candidacy.expectedCtc.toString(),
    officialNoticePeriod: candidacy.officialNoticePeriod.toString(),
    actualNoticePeriod: candidacy.actualNoticePeriod.toString(),
  };
}

export function mapFormToCandidacy(candidacyForm: CandidacyForm): UpdateCandidacyRequest {
  return {
    ...candidacyForm,
    relevantExperience: Number(candidacyForm.relevantExperience),
    expectedCtc: Number(candidacyForm.expectedCtc),
    officialNoticePeriod: Number(candidacyForm.officialNoticePeriod),
    actualNoticePeriod: Number(candidacyForm.actualNoticePeriod),
  };
}

export type CandidacyForm = Pick<
  Candidacy,
  'candidate' | 'reasonForQuickJoin' | 'remarks' | 'createdDTime'
> & {
  jobId: number;
  relevantExperience: string;
  expectedCtc: string;
  officialNoticePeriod: string;
  actualNoticePeriod: string;
};
