import {
  type Candidacy,
  type CandidacyComment,
  type CandidacyFile,
  type NewCandidacyCommentRequest,
  type NewCandidacy,
  type UpdateCandidacy,
  type CandidacyDto,
  type CandidacyStatus,
} from './schema';
import axiosApi from '../api';

const api = axiosApi();
const baseApi = '/candidacies';

export async function submitCandidacy(candidacy: NewCandidacy, files: File[] | []): Promise<void> {
  if (candidacy.candidate === null || !candidacy.candidate.pan)
    throw new Error('Candidate is required');
  const formData = new FormData();
  formData.append('jobId', candidacy.job.id.toString());
  formData.append('candidatePan', candidacy.candidate.pan);
  formData.append('relevantExperience', candidacy.relevantExperience.toString());
  formData.append('expectedCtc', candidacy.expectedCtc.toString());
  formData.append('officialNoticePeriod', candidacy.officialNoticePeriod.toString());
  formData.append('actualNoticePeriod', candidacy.actualNoticePeriod.toString());
  formData.append('reasonForQuickJoin', candidacy.reasonForQuickJoin);
  formData.append('recruiterComment', candidacy.recruiterComment);
  files.forEach((file) => {
    formData.append('files', file);
  });

  await api.post(`${baseApi}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

export const uploadFilesToCandidacy = async (id: number, files: File[]): Promise<void> => {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append('files', file);
  });

  await api.post(`${baseApi}/${id}/files`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export const withdrawCandidacy = async (id: number): Promise<void> => {
  const payload: { status: CandidacyStatus } = { status: 'WITHDRAWN' };
  await api.put(`${baseApi}/status/${id}`, payload);
};

export const rejectCandidacy = async (id: number): Promise<void> => {
  const payload: { status: CandidacyStatus } = { status: 'REJECTED' };
  await api.put(`${baseApi}/status/${id}`, payload);
};

export const acceptCandidacy = async (id: number): Promise<void> => {
  const payload: { status: CandidacyStatus } = { status: 'ACCEPTED' };
  await api.put(`${baseApi}/status/${id}`, payload);
};

export const archiveCandidacy = async (id: number): Promise<void> => {
  const payload: { status: CandidacyStatus } = { status: 'ARCHIVED' };
  await api.put(`${baseApi}/status/${id}`, payload);
};

export const reopenCandidacy = async (id: number): Promise<void> => {
  const payload: { status: CandidacyStatus } = { status: 'SENT_TO_CLIENT' };
  await api.put(`${baseApi}/status/${id}`, payload);
};

export async function updateCandidacy(id: number, candidacy: UpdateCandidacy): Promise<Candidacy> {
  const { data } = await api.put(`${baseApi}/${id}`, candidacy);
  return data;
}

export async function getCandidacy(id: number): Promise<CandidacyDto> {
  const { data } = await api.get(`${baseApi}/${id}`);
  return data;
}

export async function getAllCandidacies(): Promise<CandidacyDto[]> {
  const { data } = await api.get(`${baseApi}`);
  return data;
}

export async function getCandidacyComments(id: number): Promise<CandidacyComment[]> {
  const { data } = await api.get(`${baseApi}/${id}/comments`);
  return data;
}

export async function addCandidacyComment(
  id: number,
  comment: NewCandidacyCommentRequest
): Promise<void> {
  await api.post(`${baseApi}/${id}/comments`, comment);
}

export const deleteCandidacy = async (id: number): Promise<void> => {
  await api.delete(`${baseApi}/${id}`);
};

export const getCandidacyFiles = async (id: number): Promise<CandidacyFile[]> => {
  const { data } = await api.get(`${baseApi}/${id}/files`);
  return data;
};

export const deleteFile = async (fileId: number): Promise<void> => {
  await api.delete(`${baseApi}/files/${fileId}`);
};

export const getFile = async (fileId: number): Promise<string> => {
  const res = await api.get(`${baseApi}/files/${fileId}`, {
    responseType: 'arraybuffer',
  });
  // Get blob content as array buffer
  const blobContent = res.data;

  // Create Blob object from array buffer
  const blob = new Blob([blobContent], { type: 'application/pdf' });

  // Generate URL for Blob object
  const blobUrl = URL.createObjectURL(blob);

  // Return Blob URL
  return blobUrl;
};
