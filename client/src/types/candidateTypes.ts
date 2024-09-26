export type CandidateStatus = 'ACTIVE' | 'ARCHIVED';

export interface Candidate {
  pan: string;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
}

//request
export type NewCandidate = Candidate;
export type CandidateDto = Candidate & {
  status: CandidateStatus;
  createdAt: Date;
  updatedAt: Date;
};

// response
export type CandidateResponse = { pan: string; candidate: CandidateDto };
