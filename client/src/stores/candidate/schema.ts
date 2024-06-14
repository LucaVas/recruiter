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
  status: CandidateStatus;
  createdAt: Date;
  updatedAt: Date;
};

//request
export type NewCandidateRequest = Omit<Candidate, 'status' | 'createdAt' | 'modifiedDTime'>;
export type UpdateCandidateRequest = NewCandidateRequest;

// response
export type CandidateResponse = { pan: string; candidate: Candidate };
