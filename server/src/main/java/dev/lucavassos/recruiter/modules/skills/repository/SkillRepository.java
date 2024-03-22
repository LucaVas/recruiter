package dev.lucavassos.recruiter.modules.skills.repository;

import dev.lucavassos.recruiter.modules.skills.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepository extends JpaRepository<Skill, Long> {
}
