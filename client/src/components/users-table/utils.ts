export const getSeverity = (status: boolean) => {
  switch (status) {
    case true:
      return 'success';

    case false:
      return 'danger';
  }
};

export const formatDate = (value: string) => {
  const date = new Date(Date.parse(value));
  const formattedDate = date.toLocaleDateString('en-US', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
  return formattedDate;
};

// const getContractType = (type: string) => {
//   switch (type.toLowerCase()) {
//     case 'PERMANENT':
//       return 'info';

//     case 'TEMPORARY':
//       return 'pending';

//     default:
//       return 'info';
//   }
// };
