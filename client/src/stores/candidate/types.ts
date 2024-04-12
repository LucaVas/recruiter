export type CandidateDto = {
  id: number;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  education: string;
  currentCtc: number;
  pan: string;
  createdAt: Date;
};

export type RawCandidateDto = Omit<CandidateDto, 'id' | 'createdAt'>;

export type Recruiter = {
  id: number;
  username: string;
};
export type CandidateResponse = { id: number; candidate: CandidateDto };

