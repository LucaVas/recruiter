import type { User, UserApprovalRequest } from './types';
import axiosApi from '../api';

// vars
const api = axiosApi();

// functions
export async function approveUser(approvalRequest: UserApprovalRequest): Promise<void> {
  await api.post(`/users/approvals`, approvalRequest);
}
export async function getAllUsers(): Promise<User[]> {
  const { data } = await api.get('/users');
  return data;
}
