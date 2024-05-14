import {
  type Candidacy,
  type CandidacyComment,
  type CandidacyFile,
  type NewCandidacyCommentRequest,
  type NewCandidacyRequest,
  type UpdateCandidacyRequest,
} from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function submitCandidacy(
  candidacy: NewCandidacyRequest,
  file: File | undefined
): Promise<void> {
  const formData = new FormData();
  formData.append('jobId', candidacy.jobId.toString());
  formData.append('candidatePan', candidacy.candidatePan);
  formData.append('relevantExperience', candidacy.relevantExperience.toString());
  formData.append('expectedCtc', candidacy.expectedCtc.toString());
  formData.append('officialNoticePeriod', candidacy.officialNoticePeriod.toString());
  formData.append('actualNoticePeriod', candidacy.actualNoticePeriod.toString());
  formData.append('reasonForQuickJoin', candidacy.reasonForQuickJoin);
  formData.append('recruiterComment', candidacy.recruiterComment);
  if (candidacy.status) formData.append('status', candidacy.status.toString());
  if (file !== undefined) formData.append('resume', file);

  await api.post(`/candidacies`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

export async function updateCandidacy(
  jobId: number,
  pan: string,
  candidacy: UpdateCandidacyRequest
): Promise<Candidacy> {
  const { data } = await api.put(`/candidacies/job=${jobId}&candidate=${pan}`, candidacy);
  return data;
}

export async function getCandidacy(jobId: number, pan: string): Promise<Candidacy> {
  const { data } = await api.get(`/candidacies/job=${jobId}&candidate=${pan}`);
  return data;
}

export async function getAllCandidacies(): Promise<Candidacy[]> {
  const { data } = await api.get(`/candidacies`);
  return data;
}

export async function getCandidacyComments(
  jobId: number,
  pan: string
): Promise<CandidacyComment[]> {
  const { data } = await api.get(`/candidacies/job=${jobId}&candidate=${pan}/comments`);
  return data;
}

export async function addCandidacyComment(
  jobId: number,
  pan: string,
  comment: NewCandidacyCommentRequest
): Promise<void> {
  await api.post(`/candidacies/job=${jobId}&candidate=${pan}/comments`, comment);
}

export const deleteCandidacy = async (jobId: number, pan: string): Promise<void> => {
  await api.delete(`/candidacies/job=${jobId}&candidate=${pan}`);
};

export const getCandidacyFiles = async (jobId: number, pan: string): Promise<CandidacyFile[]> => {
  const { data } = await api.get(`/candidacies/job=${jobId}&candidate=${pan}/files`);
  return data;
};

export const deleteFile = async (fileId: number): Promise<void> => {
  await api.delete(`/candidacies/files/${fileId}`);
};

export const getFileUrl = async (fileId: number): Promise<string> => {
  const { data } = await api.get(`/candidacies/files/${fileId}/url`);
  return data;
};
