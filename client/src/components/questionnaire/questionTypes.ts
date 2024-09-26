import type { QuestionType } from '@/types/questionTypes';
import { ref } from 'vue';

export const questionTypes = ref<{ value: QuestionType; label: string }[]>([
  { value: 'SHORT', label: 'Short Answer' },
  { value: 'PARAGRAPH', label: 'Paragraph' },
  { value: 'YES_NO', label: 'Yes / No' },
  { value: 'OPEN_QUESTION', label: 'Open Question' },
]);
