import type { Client, NewClient } from '../types/clientTypes';
import axiosApi from './baseApi';

const api = axiosApi();
const baseApi = '/clients';

export async function getAllClients(): Promise<Client[]> {
  const { data } = await api.get(`${baseApi}`);
  return data;
}

export const createClient = async (newClient: NewClient): Promise<Client> => {
  const { data } = await api.post(`${baseApi}`, newClient);
  return data;
};
