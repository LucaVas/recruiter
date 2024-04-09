export const formatDate = (value: string) => {
  if (!value || value === '') return '-';
  const date = new Date(Date.parse(value));
  const formattedDate = date.toLocaleDateString('en-hi', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
  return formattedDate;
};
