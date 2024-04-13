// backend dtos
export type Candidate = {
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
  pan: string;
  createdAt: Date;
};

// backend domain objects
export type CandidateResponse = { pan: string; candidate: Candidate };
export type NewCandidateRequest = Omit<Candidate, 'createdAt'>
export type UpdateCandidateRequest = NewCandidateRequest

// backend enums
export type CandidateStatus = 'ACTIVE' | 'ARCHIVED'
