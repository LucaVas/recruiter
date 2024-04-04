import type { SkillDto } from '@/stores/skill/types';

export const getSkills = (skills: SkillDto[]): string => {
  return skills.map((skill) => skill.name).join(', ');
};
