import type { NewClient } from '@/stores/client/schema';
import { ValidationError } from '@/utils/types';

export const validateClient = (client: NewClient) => {
  const errors: Record<string, string> = {};

  if (!client.name) {
    errors.name = 'Name is required';
  }

  if (!client.industry) {
    errors.industry = 'Industry is required';
  }

  if (Object.keys(errors).length > 0) throw new ValidationError('Validation failed', errors);
};
