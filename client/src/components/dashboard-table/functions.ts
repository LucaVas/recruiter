import type { SkillDto } from '@/stores/skill/types';
import { useRouter } from 'vue-router';

const router = useRouter();

export const getSkills = (skills: SkillDto[]): string => {
  return skills.map((skill) => skill.name).join(', ');
};

export const applyToJob = (jobId: number): void => {
  router.push({ name: 'NewCandidacy', params: { jobId: jobId } });
};

