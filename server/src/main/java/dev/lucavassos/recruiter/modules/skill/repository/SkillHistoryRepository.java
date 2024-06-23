package dev.lucavassos.recruiter.modules.skill.repository;

import dev.lucavassos.recruiter.modules.skill.entities.SkillHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillHistoryRepository extends JpaRepository<SkillHistory, Long> {
}
