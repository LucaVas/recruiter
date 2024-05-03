import type { CandidacyStatus } from '@/stores/candidacy/schema';

export const getCandidacyStatus = (status: CandidacyStatus): string => {
  if (status === 'ACCEPTED') return 'Accepted';
  if (status === 'REJECTED') return 'Rejected';
  if (status === 'SENT_TO_CLIENT') return 'Sent to client';
  return '';
};

export const getCandidacyStatusSeverity = (status: CandidacyStatus): string => {
    if (status === 'ACCEPTED') return 'success';
    if (status === 'REJECTED') return 'danger';
    if (status === 'SENT_TO_CLIENT') return 'secondary';
    return '';
}
