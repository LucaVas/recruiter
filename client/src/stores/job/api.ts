import axiosApi from '../api';
import type { Job, JobStatus, NewJob } from './schema';

const api = axiosApi();
const baseApi = '/jobs';

export class JobApi {
  async getJob(id: number): Promise<Job> {
    const { data } = await api.get(`${baseApi}/${id}`);
    return data;
  }
  async getAllJobs(): Promise<Job[]> {
    const { data } = await api.get(baseApi);
    return data;
  }
  async addJob(newJob: NewJob): Promise<Job> {
    const { data } = await api.post(baseApi, newJob);
    return data;
  }
  async updateJob(id: number, job: Job): Promise<Job> {
    const { data } = await api.put(`${baseApi}/${id}`, job);
    return data;
  }
  async changeJobStatus(id: number, newStatus: JobStatus) {
    await api.post(`${baseApi}/status/${id}`, { status: newStatus });
  }
  async deleteJob(id: number) {
    await api.delete(`${baseApi}/${id}`);
  }
}
