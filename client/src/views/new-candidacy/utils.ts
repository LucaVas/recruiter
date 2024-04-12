import type { CandidateForm } from '.';

export function generateEmptyCandidateForm(): CandidateForm {
  return {
    name: '',
    phone: '',
    email: '',
    totalExperience: '',
    education: '',
    currentCtc: '',
    pan: '',
  };
}
