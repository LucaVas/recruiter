export const getSeverity = (status: boolean) => {
  switch (status) {
    case true:
      return 'success';

    case false:
      return 'danger';
  }
};

export const formatDate = (value: string) => {
  if (!value || value === '') return '-';
  const date = new Date(Date.parse(value));
  const formattedDate = date.toLocaleDateString('en-US', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
  return formattedDate;
};
