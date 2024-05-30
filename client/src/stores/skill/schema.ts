export type Skill = {
  id: number;
  name: string;
};

export type NewSkill = Pick<Skill, 'name'>;
