export const capitalize = (word: string) =>
  word.toLowerCase().charAt(0).toUpperCase() + word.slice(1);

export const capitalizeText = (text: string) =>
  text
    .split(' ')
    .map((word, index) => (index === 0 ? capitalize(word) : word))
    .join(' ');

export const capitalizeWords = (text: string) =>
  text
    .split(' ')
    .map((word) => capitalize(word))
    .join(' ');
