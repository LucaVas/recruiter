export type candidateStatus = 'ACTIVE' | 'ARCHIVED';

// backend dtos
export type Candidate = {
  pan: string;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
  createdDTime: Date;
  updatedDTime: Date;
};

//request
export type NewCandidateRequest = Omit<Candidate, 'createdDTime' | 'updatedDTime'>;
export type UpdateCandidateRequest = NewCandidateRequest

// response
export type CandidateResponse = { pan: string; candidate: Candidate };
