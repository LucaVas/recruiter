import type { RawSkill, Skill } from './types';

export function toRaw(skill: Skill): RawSkill {
  return {
    id: skill.id,
    name: skill.name,
  };
}
