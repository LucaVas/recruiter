export const formatDate = (value: string | Date) => {
  if (!value || value === '') return '-';
  const date = new Date(Date.parse(value as string));
  const formattedDate = date.toLocaleDateString('en-hi', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
  return formattedDate;
};

export const formatDateTime = (value: string | Date) => {
  if (!value || value === '') return '-';
  const date = new Date(Date.parse(value as string));
  const formattedDate = date.toLocaleDateString('en-hi', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
  return formattedDate;
};
