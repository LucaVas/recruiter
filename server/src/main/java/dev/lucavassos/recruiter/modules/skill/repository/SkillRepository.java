package dev.lucavassos.recruiter.modules.skill.repository;

import dev.lucavassos.recruiter.modules.skill.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepository extends JpaRepository<Skill, Long> {
}
