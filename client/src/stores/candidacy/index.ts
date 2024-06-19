import {
  type Candidacy,
  type CandidacyComment,
  type CandidacyFile,
  type NewCandidacyCommentRequest,
  type NewCandidacy,
  type UpdateCandidacy,
} from './schema';
import axiosApi from '../api';

const api = axiosApi();
const baseApi = '/candidacies';

export async function submitCandidacy(
  candidacy: NewCandidacy,
  file: File | undefined
): Promise<void> {
  if (candidacy.candidate === null || !candidacy.candidate.pan) throw new Error('Candidate is required');
  const formData = new FormData();
  formData.append('jobId', candidacy.job.id.toString());
  formData.append('candidatePan', candidacy.candidate.pan);
  formData.append('relevantExperience', candidacy.relevantExperience.toString());
  formData.append('expectedCtc', candidacy.expectedCtc.toString());
  formData.append('officialNoticePeriod', candidacy.officialNoticePeriod.toString());
  formData.append('actualNoticePeriod', candidacy.actualNoticePeriod.toString());
  formData.append('reasonForQuickJoin', candidacy.reasonForQuickJoin);
  formData.append('recruiterComment', candidacy.recruiterComment);
  if (file !== undefined) formData.append('resume', file);

  await api.post(`${baseApi}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

export const uploadFilesToCandidacy = async (
  jobId: number,
  pan: string,
  files: File[]
): Promise<void> => {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append('files', file);
  });

  await api.post(`${baseApi}/job=${jobId}&candidate=${pan}/files`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

export async function updateCandidacy(
  jobId: number,
  pan: string,
  candidacy: UpdateCandidacy
): Promise<Candidacy> {
  const { data } = await api.put(`${baseApi}/job=${jobId}&candidate=${pan}`, candidacy);
  return data;
}

export async function getCandidacy(jobId: number, pan: string): Promise<Candidacy> {
  const { data } = await api.get(`${baseApi}/job=${jobId}&candidate=${pan}`);
  return data;
}

export async function getAllCandidacies(): Promise<Candidacy[]> {
  const { data } = await api.get(`${baseApi}`);
  return data;
}

export async function getCandidacyComments(
  jobId: number,
  pan: string
): Promise<CandidacyComment[]> {
  const { data } = await api.get(`${baseApi}/job=${jobId}&candidate=${pan}/comments`);
  return data;
}

export async function addCandidacyComment(
  jobId: number,
  pan: string,
  comment: NewCandidacyCommentRequest
): Promise<void> {
  await api.post(`${baseApi}/job=${jobId}&candidate=${pan}/comments`, comment);
}

export const deleteCandidacy = async (jobId: number, pan: string): Promise<void> => {
  await api.delete(`${baseApi}/job=${jobId}&candidate=${pan}`);
};

export const getCandidacyFiles = async (jobId: number, pan: string): Promise<CandidacyFile[]> => {
  const { data } = await api.get(`${baseApi}/job=${jobId}&candidate=${pan}/files`);
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
