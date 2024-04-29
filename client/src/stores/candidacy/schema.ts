export type Candidacy = {
  id: number;
  relevantExperience: number;
  expectedCtc: number;
  officialNoticePeriod: number;
  actualNoticePeriod: number;
  reasonForQuickJoin: string;
  remarks: string;
  modifiedDTime: Date;
  createdDTime: Date;
};

export type candidacyComment = {
  id: number;
  text: string;
  createdDTime: Date;
  modifiedDTime: Date;
};

// request
export type UpdateCandidacyRequest = Omit<Candidacy, 'id' | 'createdDTime' | 'modifiedDTime'>;
export type NewCandidacyRequest = UpdateCandidacyRequest & { jobId: number; candidatePan: string };

// response
export type CandidacyResponse = {
  candidacy: Candidacy;
};

// frontend types
export type RawCandidacy = UpdateCandidacyRequest;
