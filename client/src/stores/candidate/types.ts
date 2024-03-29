export type Candidate = {
  id: number;
  name: string;
  phone: string;
  email: string;
  totalExperience: number;
  relevantExperience: number;
  education: string;
  currentCtc: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  remarks: string;
  recruiter: Recruiter;
  pan: string;
  comments: string;
  createdAt: string;
};

export type Recruiter = {
  id: number;
  username: string;
};
export type CandidateResponse = { id: number; candidate: Candidate };

