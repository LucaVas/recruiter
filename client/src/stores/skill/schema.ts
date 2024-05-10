export type Skill = {
  id: number;
  name: string;
  createdDTime: Date;
  modifiedDTime: Date;
};

export type NewSkill = Pick<Skill, 'name'>;
