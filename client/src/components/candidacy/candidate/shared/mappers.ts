import type { RawCandidateDto } from "@/stores/candidate/types";
import type { CandidateForm } from "@/views/candidacy";

export function mapFormToNewCandidate(details: CandidateForm): RawCandidateDto {
  return {
    ...details,
    totalExperience: Number(details.totalExperience),
    currentCtc: Number(details.currentCtc),
  };
}

export function mapCandidateToForm(candidate: RawCandidateDto): CandidateForm {
  return {
    ...candidate,
    totalExperience: candidate.totalExperience.toString(),
    currentCtc: candidate.currentCtc.toString(),
  };
}
