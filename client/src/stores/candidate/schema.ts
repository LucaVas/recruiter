export type CandidateStatus = 'ACTIVE' | 'ARCHIVED';

// backend dtos
export type Candidate = {
  pan: string;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
  status: CandidateStatus,
  createdDTime: Date;
  modifiedDTime: Date;
};

//request
export type NewCandidateRequest = Omit<Candidate, 'createdDTime' | 'modifiedDTime'>;
export type UpdateCandidateRequest = NewCandidateRequest

// response
export type CandidateResponse = { pan: string; candidate: Candidate };
