import type { CandidacyStatus } from '@/stores/candidacy/schema';

export const getCandidacyStatus = (status: CandidacyStatus): string => {
  if (status === 'ACCEPTED') return 'Accepted';
  if (status === 'REJECTED') return 'Rejected';
  if (status === 'SENT_TO_CLIENT') return 'Sent to client';
  if (status === 'WITHDRAWN') return 'Withdrawn';
  if (status === 'ARCHIVED') return 'Archived';

  return '';
};

export const getCandidacyStatusSeverity = (status: CandidacyStatus): string => {
  if (status === 'ACCEPTED') return 'success';
  if (status === 'REJECTED') return 'danger';
  if (status === 'SENT_TO_CLIENT') return 'default';
  if (status === 'WITHDRAWN') return 'warning';
  if (status === 'ARCHIVED') return 'secondary';

  return '';
};

export const getCandidacyStatusIcon = (status: CandidacyStatus): string => {
  if (status === 'ACCEPTED') return 'pi pi-check-square';
  if (status === 'REJECTED') return 'pi pi-times-circle';
  if (status === 'SENT_TO_CLIENT') return 'pi pi-truck';
  if (status === 'WITHDRAWN') return 'pi pi-eject';
  if (status === 'ARCHIVED') return 'pi pi-folder';

  return '';
};
