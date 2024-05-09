import type { Client, NewClient } from './schema';
import axiosApi from '../api';

const api = axiosApi();

export async function getAllClients(): Promise<Client[]> {
  const { data } = await api.get(`/clients`);
  return data;
}

export const createClient = async (newClient: NewClient): Promise<Client> => {
  const { data } = await api.post(`/clients`, newClient);
  return data;
};
