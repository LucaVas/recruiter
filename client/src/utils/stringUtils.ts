export const capitalize = (word: string) =>
  word.toLowerCase().charAt(0).toUpperCase() + word.slice(1);

export const capitalizeText = (text: string) =>
  text
    .split(' ') // split words into array
    .map((word, index) => (index === 0 ? capitalize(word) : word)) // Capitalize the first word
    .join(' '); // Join the words back into a string

export const capitalizeWords = (text: string) =>
  text
    .split(' ') // split words into array
    .map((word) => capitalize(word)) // Capitalize all words
    .join(' '); // Join the words back into a string
